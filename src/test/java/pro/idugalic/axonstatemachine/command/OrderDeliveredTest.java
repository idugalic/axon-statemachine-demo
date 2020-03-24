package pro.idugalic.axonstatemachine.command;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.event.OrderDeliveredEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.event.UnsupportedOperationEvent;

import java.math.BigDecimal;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class OrderDeliveredTest {

    private static ArrayList<OrderItem> items;
    private FixtureConfiguration<OrderDelivered> fixture = new AggregateTestFixture<>(OrderDelivered.class);

    @BeforeAll
    static void before() {
        items = new ArrayList<>();
        items.add(new OrderItem("id1", "name1", 1L, BigDecimal.TEN));
        items.add(new OrderItem("id2", "name2", 3L, BigDecimal.ONE));
    }

    @Test
    public void orderCancellationRefusedTest() {
        OrderDeliveredEvent orderDeliveredEvent = new OrderDeliveredEvent("aggregate-order1", "order1", OrderStatus.PAID, items);
        MarkOrderAsCancelledCommand markOrderAsCancelledCommand = new MarkOrderAsCancelledCommand("aggregate-order1");
        UnsupportedOperationEvent unsupportedOperationEvent = new UnsupportedOperationEvent("aggregate-order1",null, MarkOrderAsCancelledCommand.class);

        fixture.given(orderDeliveredEvent)
               .when(markOrderAsCancelledCommand)
               .expectEvents(unsupportedOperationEvent)
               .expectNotMarkedDeleted();
    }
}
