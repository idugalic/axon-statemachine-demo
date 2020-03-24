package pro.idugalic.axonstatemachine.command;

import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;
import pro.idugalic.axonstatemachine.command.api.command.AddItemToTheOrderCommand;
import pro.idugalic.axonstatemachine.command.api.command.CreateOrderCommand;
import pro.idugalic.axonstatemachine.command.api.event.ItemAddedEvent;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsPaidCommand;
import pro.idugalic.axonstatemachine.command.api.event.OrderCanceledEvent;
import pro.idugalic.axonstatemachine.command.api.event.OrderCancellationInitiatedEvent;
import pro.idugalic.axonstatemachine.command.api.event.OrderCreatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.event.OrderPaidEvent;
import pro.idugalic.axonstatemachine.command.api.event.OrderPayingInitiatedEvent;

import java.math.BigDecimal;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class OrderCreatedTest {

    private static ArrayList<OrderItem> items;
    private FixtureConfiguration<OrderCreated> fixture = new AggregateTestFixture<>(OrderCreated.class);

    @BeforeAll
    static void before() {
        items = new ArrayList<>();
        items.add(new OrderItem("id1", "name1", 1L, BigDecimal.TEN));
        items.add(new OrderItem("id2", "name2", 3L, BigDecimal.ONE));
    }

    @Test
    public void orderCreatedTest() {
        CreateOrderCommand createOrderCommand = new CreateOrderCommand("aggregate-order1", "order1", items);
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1", "order1", OrderStatus.NULL, items);

        fixture.given()
               .when(createOrderCommand)
               .expectEvents(orderCreatedEvent);
    }

    @Test
    public void itemAddedTest() {

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1", "order1", OrderStatus.NULL, items);
        OrderItem orderItem = new OrderItem("3", "orederItem1", 1L, BigDecimal.TEN);
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

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1", "order1", OrderStatus.NULL, items);
        MarkOrderAsCancelledCommand markOrderAsCancelledCommand = new MarkOrderAsCancelledCommand("aggregate-order1");
        OrderCancellationInitiatedEvent orderCancellationInitiatedEvent = new OrderCancellationInitiatedEvent("aggregate-order1");
        OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent("", "order1", OrderStatus.NEW, items);

        fixture.given(orderCreatedEvent)
               .when(markOrderAsCancelledCommand)
               .expectSuccessfulHandlerExecution()
               .expectEvents(orderCancellationInitiatedEvent, orderCanceledEvent)
               .expectMarkedDeleted();
    }

    @Test
    public void orderPaidTest() throws Exception {

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1", "order1", OrderStatus.NULL, items);
        MarkOrderAsPaidCommand markOrderAsPaidCommand = new MarkOrderAsPaidCommand("aggregate-order1");
        OrderPayingInitiatedEvent orderPayingInitiatedEvent = new OrderPayingInitiatedEvent("aggregate-order1");
        OrderPaidEvent orderPaidEvent = new OrderPaidEvent("", "order1", OrderStatus.NEW, items);

        fixture.given(orderCreatedEvent)
               .when(markOrderAsPaidCommand)
               .expectSuccessfulHandlerExecution()
               .expectEvents(orderPayingInitiatedEvent, orderPaidEvent)
               .expectMarkedDeleted();
    }
}
