package pro.idugalic.axonstatemachine.command;

import org.axonframework.eventsourcing.IncompatibleAggregateException;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsDeliveredCommand;
import pro.idugalic.axonstatemachine.command.api.OrderCancelationInitiatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderCanceledEvent;
import pro.idugalic.axonstatemachine.command.api.OrderCreatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderDeliveredEvent;
import pro.idugalic.axonstatemachine.command.api.OrderDeliveryInitiatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.OrderPaidEvent;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderPaidTest {

    private static List<OrderItem> items;
    private FixtureConfiguration<OrderPaid> fixture = new AggregateTestFixture<>(OrderPaid.class);

    @BeforeAll
    static void before() {
        items = Arrays.asList(new OrderItem("id1", "name1", 1L, BigDecimal.TEN),
                              new OrderItem("id2", "name2", 3L, BigDecimal.ONE));
    }

    @Test
    public void orderPaidTest() {
        OrderPaidEvent orderPaidEvent = new OrderPaidEvent("aggregate-order1", "order1", items);
        MarkOrderAsDeliveredCommand markOrderAsDeliveredCommand = new MarkOrderAsDeliveredCommand("aggregate-order1");
        OrderDeliveryInitiatedEvent orderDeliveryInitiatedEvent = new OrderDeliveryInitiatedEvent("aggregate-order1");
        OrderDeliveredEvent orderDeliveredEvent = new OrderDeliveredEvent("aggregate-order1","order1", items);

        fixture.given(orderPaidEvent)
               .when(markOrderAsDeliveredCommand)
               .expectEvents(orderDeliveryInitiatedEvent, orderDeliveredEvent)
               .expectMarkedDeleted();
    }

    @Test
    public void orderCanceledTest() {

        OrderPaidEvent orderPaidEvent = new OrderPaidEvent("aggregate-order1", "order1", items);
        MarkOrderAsCancelledCommand markOrderAsCancelledCommand = new MarkOrderAsCancelledCommand("aggregate-order1");
        OrderCancelationInitiatedEvent orderCancelationInitiatedEvent = new OrderCancelationInitiatedEvent("aggregate-order1");
        OrderCanceledEvent orderCanceledEvent = new OrderCanceledEvent("", "order1", items);

        fixture.given(orderPaidEvent)
               .when(markOrderAsCancelledCommand)
               .expectSuccessfulHandlerExecution()
               .expectEvents(orderCancelationInitiatedEvent, orderCanceledEvent)
               .expectMarkedDeleted();
    }

    @Test
    public void orderNotCanceled_IncompatibleAggregateExceptionTest() {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1", "order1", items);
        MarkOrderAsCancelledCommand markOrderAsCancelledCommand = new MarkOrderAsCancelledCommand("aggregate-order1");

        fixture.given(orderCreatedEvent)
               .when(markOrderAsCancelledCommand)
               .expectException(IncompatibleAggregateException.class);
    }

    @Test
    public void orderNotCanceled_AggregateNotFoundExceptionTest() {
        MarkOrderAsCancelledCommand markOrderAsCancelledCommand = new MarkOrderAsCancelledCommand("aggregate-order1");

        fixture.given()
               .when(markOrderAsCancelledCommand)
               .expectException(AggregateNotFoundException.class);
    }

}
