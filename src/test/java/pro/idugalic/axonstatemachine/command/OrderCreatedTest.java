package pro.idugalic.axonstatemachine.command;

import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;
import pro.idugalic.axonstatemachine.command.api.AddItemToTheOrderCommand;
import pro.idugalic.axonstatemachine.command.api.CreateOrderCommand;
import pro.idugalic.axonstatemachine.command.api.ItemAddedEvent;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsPaidCommand;
import pro.idugalic.axonstatemachine.command.api.OrderCancelationInitiatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderCanceledEvent;
import pro.idugalic.axonstatemachine.command.api.OrderCreatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.OrderPaidEvent;
import pro.idugalic.axonstatemachine.command.api.OrderPayingInitiatedEvent;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderCreatedTest {

    private static List<OrderItem> items;
    private FixtureConfiguration<OrderCreated> fixture = new AggregateTestFixture<>(OrderCreated.class);

    @BeforeAll
    static void before() {
        items = Arrays.asList(new OrderItem("id1", "name1", 1L, BigDecimal.TEN),
                              new OrderItem("id2", "name2", 3L, BigDecimal.ONE));
    }

    @Test
    public void orderCreatedTest() {
        CreateOrderCommand createOrderCommand = new CreateOrderCommand("aggregate-order1", "order1", items);
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1", "order1", items);

        fixture.given()
               .when(createOrderCommand)
               .expectEvents(orderCreatedEvent);
    }

    @Test
    public void itemAddedTest() {

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1", "order1", items);
        OrderItem orderItem = new OrderItem("3", "orederItem1", 1l, BigDecimal.TEN);
        AddItemToTheOrderCommand addItemToTheOrderCommand = new AddItemToTheOrderCommand("aggregate-order1", "order1", orderItem);
        ItemAddedEvent itemAddedEvent = new ItemAddedEvent("aggregate-order1", "order1", orderItem);

        fixture.given(orderCreatedEvent)
               .when(addItemToTheOrderCommand)
               .expectSuccessfulHandlerExecution()
               .expectEvents(itemAddedEvent);
    }

    @Test
    public void itemNotAdded_AggregateNotFoundExceptionTest() {

        OrderItem orderItem = new OrderItem("1", "orederItem1", 1L, BigDecimal.TEN);
        AddItemToTheOrderCommand addItemToTheOrderCommand = new AddItemToTheOrderCommand("aggregate-order1", "order1", orderItem);

        fixture.given()
               .when(addItemToTheOrderCommand)
               .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void orderCanceledTest() {

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1", "order1", items);
        MarkOrderAsCancelledCommand markOrderAsCancelledCommand = new MarkOrderAsCancelledCommand("aggregate-order1");
        OrderCancelationInitiatedEvent orderCancelationInitiatedEvent = new OrderCancelationInitiatedEvent("aggregate-order1");
        OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent("", "order1", items);

        fixture.given(orderCreatedEvent)
               .when(markOrderAsCancelledCommand)
               .expectSuccessfulHandlerExecution()
               .expectEvents(orderCancelationInitiatedEvent, orderCanceledEvent)
               .expectMarkedDeleted();
    }

    @Test
    public void orderPaidTest() throws Exception {

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1", "order1", items);
        MarkOrderAsPaidCommand markOrderAsPaidCommand = new MarkOrderAsPaidCommand("aggregate-order1");
        OrderPayingInitiatedEvent orderPayingInitiatedEvent = new OrderPayingInitiatedEvent("aggregate-order1");
        OrderPaidEvent orderPaidEvent = new OrderPaidEvent("", "order1", items);

        fixture.given(orderCreatedEvent)
               .when(markOrderAsPaidCommand)
               .expectSuccessfulHandlerExecution()
               .expectEvents(orderPayingInitiatedEvent, orderPaidEvent)
               .expectMarkedDeleted();
    }
}
