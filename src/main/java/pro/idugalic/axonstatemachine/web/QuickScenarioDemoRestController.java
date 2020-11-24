package pro.idugalic.axonstatemachine.web;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;
import pro.idugalic.axonstatemachine.command.api.command.CreateOrderCommand;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsDeliveredCommand;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsPaidCommand;
import pro.idugalic.axonstatemachine.query.api.GetAllStateTransitionAggregateIdsRequest;
import pro.idugalic.axonstatemachine.query.api.GetAllStateTransitionAggregateIdsResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Exposes a number of different endpoints which run end-to-end scenarios.
 *
 * The purpose is for you to then interactively explore the Axon server event store
 * to understand what events have been logged.
 *
 * Where possible we log as much as possible in order to see what is happening in the system.
 */
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/quick-demo")
public class QuickScenarioDemoRestController {
    private static final Logger log = LoggerFactory.getLogger(QuickScenarioDemoRestController.class);

    /**
     * Threadsafe long generator used to create IDs when needed.
     */
    private final AtomicLong idGenerator;

    /**
     * Used to issue commands.
     */
    private final CommandGateway commandGateway;

    /**
     * Query gateway we will issue queries to.
     */
    private final QueryGateway queryGateway;

    public QuickScenarioDemoRestController(
            CommandGateway commandGateway,
            QueryGateway queryGateway) {
        this.idGenerator = new AtomicLong();
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    /**
     * Simple creation of a random new order.
     */
    @GetMapping(value = "/orders/new-random")
    public CreateOrderCommand createRandomOrder() {
        // 1. Generate somewhere between 1 to 3 random order items.
        ArrayList<OrderItem> orderItems = IntStream.range(1, 4)
                .mapToObj(i -> randomOrderItem())
                .collect(Collectors.toCollection(ArrayList::new));
        log.info("Created {} new random order items: {}", orderItems.size(), orderItems);

        // 2. Create the command and issue it.
        CreateOrderCommand createOrderCommand = new CreateOrderCommand(RandomStringUtils.randomAlphabetic(8), orderItems);
        log.info("Issuing command: {}", createOrderCommand);
        commandGateway.sendAndWait(createOrderCommand);

        // 3. Return the command that was used to create the order
        return createOrderCommand;
    }

    /**
     * End to end scenario of:
     * 1. (OrderCreated)   creating an order
     * 2. (OrderCancelled) cancelling that order
     */
    @GetMapping(value = "/orders/cancel-scenario")
    public String cancelOrderScenario() {
        // 1. Create a new random order with given aggregate ID.
        CreateOrderCommand randomOrder = createRandomOrder();
        UUID aggregateId = UUID.fromString(randomOrder.getAggregateIdentifier());

        // 2. Cancel the newly created order.
        MarkOrderAsCancelledCommand cancelCmd = new MarkOrderAsCancelledCommand(aggregateId.toString());
        commandGateway.sendAndWait(cancelCmd);

        // 3. Return string back to caller.
        return "OrderCancelled scenario complete";
    }


    /**
     * End to end scenario of:
     * 1. (OrderCreated)   creating an order
     * 2. (OrderPaid)      paying for the order
     * 3. (OrderCancelled) cancelling that order (will intentionally fail for this scenario)
     */
    @GetMapping(value = "/orders/cancel-after-paying-scenario-with-intentional-failure")
    public void cancelOrderAfterPayingScenarioWithFailure() {
        // 1. Create a new random order with given aggregate ID.
        CreateOrderCommand randomOrder = createRandomOrder();
        UUID aggregateId = UUID.fromString(randomOrder.getAggregateIdentifier());

        // 2. Mark the order as paid.
        MarkOrderAsPaidCommand cmd = new MarkOrderAsPaidCommand(aggregateId.toString());
        log.info("Issuing command: {}", cmd);
        commandGateway.sendAndWait(cmd);

        // 3. Cancel the paid order with incorrect aggregateId.
        // N.B. since we are using the aggregate identifier of the CreateOrderCommand we will get:
        //      `CommandExecutionException(Aggregate with identifier [<some-uuid>] not found.
        //       It has been deleted.` If we wanted this to succeed we would need to use the aggregate
        //       identifier of the OrderPaid aggregate.
        MarkOrderAsCancelledCommand cancelCmd = new MarkOrderAsCancelledCommand(aggregateId.toString());
        commandGateway.send(cancelCmd);
    }

    /**
     * End to end scenario of:
     * 1. (OrderCreated)   creating an order
     * 2. (OrderPaid)      paying for the order
     * 3. (OrderCancelled) cancelling that order
     */
    @GetMapping(value = "/orders/cancel-after-paying-scenario")
    public String cancelOrderAfterPayingScenarioWithSuccess() throws ExecutionException, InterruptedException {
        // 1. Create a new random order with given aggregate ID.
        CreateOrderCommand randomOrder = createRandomOrder();
        String orderId = randomOrder.getOrderId();
        UUID aggregateId = UUID.fromString(randomOrder.getAggregateIdentifier());

        // 2. Mark the order as paid.
        MarkOrderAsPaidCommand cmd = new MarkOrderAsPaidCommand(aggregateId.toString());
        log.info("Issuing command: {}", cmd);
        commandGateway.sendAndWait(cmd);

        // Wait a second for the projection(s) to be processed.
        Thread.sleep(1_000);

        // Retrieve the latest IDs associated with each state for the orderId.
        ConcurrentHashMap<OrderStatus, UUID> stateToAggregateIdMap = queryForStateToAggregateIdMap(orderId);

        // 3. Cancel the paid order with the aggregateId of the PAID state (throwing NPE if no PAID state found).
        UUID paidStateAggregateId = stateToAggregateIdMap.get(OrderStatus.PAID);
        MarkOrderAsCancelledCommand cancelCmd = new MarkOrderAsCancelledCommand(paidStateAggregateId.toString());
        commandGateway.send(cancelCmd);

        // Return string back to caller.
        return "cancel after paying scenario complete";
    }

    /**
     * End to end scenario of:
     * 1. (OrderCreated)   creating an order
     * 2. (OrderPaid)      paying for the order
     * 3. (OrderDelivered) the order is delivered
     */
    @GetMapping(value = "/orders/order-delivered-scenario")
    public String orderDeliveredScenario() throws ExecutionException, InterruptedException {
        // 1. Create a new random order with given aggregate ID.
        CreateOrderCommand randomOrder = createRandomOrder();
        String orderId = randomOrder.getOrderId();
        UUID aggregateId = UUID.fromString(randomOrder.getAggregateIdentifier());

        // 2. Mark the order as paid.
        MarkOrderAsPaidCommand cmd = new MarkOrderAsPaidCommand(aggregateId.toString());
        log.info("Issuing command: {}", cmd);
        commandGateway.sendAndWait(cmd);

        // Wait a second for the projection(s) to be processed.
        Thread.sleep(1_000);

        // Retrieve the latest IDs associated with each state for the orderId.
        ConcurrentHashMap<OrderStatus, UUID> stateToAggregateIdMap = queryForStateToAggregateIdMap(orderId);

        // 3. Deliver the order with the aggregateId of the PAID state.
        UUID paidStateAggregateId = stateToAggregateIdMap.get(OrderStatus.PAID);
        MarkOrderAsDeliveredCommand deliveredCommand = new MarkOrderAsDeliveredCommand(paidStateAggregateId.toString());
        commandGateway.send(deliveredCommand);

        return "order delivered scenario complete";
    }

    /* ***********************************************
     * Utility Methods
     * ***********************************************/
    private OrderItem randomOrderItem() {
        return OrderItem.builder()
                // Use a unique long ID for convenience.
                .id(String.valueOf(idGenerator.incrementAndGet()))
                // Name of the order item is just a random alphabetical string of 8 characters.
                .name(RandomStringUtils.randomAlphabetic(8))
                // Price is a random value between 0.01 and 100.
                .price(BigDecimal.valueOf(RandomUtils.nextDouble(0.01d, 100d)))
                // Quantity is random value between 1 and 5.
                .quantity(RandomUtils.nextLong(1, 6))
                .build();
    }

    private ConcurrentHashMap<OrderStatus, UUID> queryForStateToAggregateIdMap(String orderId) throws ExecutionException, InterruptedException {
        log.debug("Issuing query to retrieve all aggregate states for orderId: {}", orderId);
        GetAllStateTransitionAggregateIdsRequest query = new GetAllStateTransitionAggregateIdsRequest(orderId);
        GetAllStateTransitionAggregateIdsResponse resp = queryGateway
                .query(query, ResponseTypes.instanceOf(GetAllStateTransitionAggregateIdsResponse.class)).get();
        ConcurrentHashMap<OrderStatus, UUID> stateToAggregateId = resp.getStateToAggregateId();
        log.debug("Retrieved all aggregate states for orderId: {}, statesToAggregateId: {}", orderId, stateToAggregateId);
        return stateToAggregateId;
    }

}
