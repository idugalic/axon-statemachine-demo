package pro.idugalic.axonstatemachine.command;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.OrderCancellationRefusedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderDeliveredEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderDeliveredTest {

    private static List<OrderItem> items;
    private FixtureConfiguration<OrderDelivered> fixture = new AggregateTestFixture<>(OrderDelivered.class);

    @BeforeAll
    static void before() {
        items = Arrays.asList(new OrderItem("id1", "name1", 1L, BigDecimal.TEN),
                              new OrderItem("id2", "name2", 3L, BigDecimal.ONE));
    }

    @Test
    public void orderCancellationRefusedTest() {
        OrderDeliveredEvent orderDeliveredEvent = new OrderDeliveredEvent("aggregate-order1", "order1", items);
        MarkOrderAsCancelledCommand markOrderAsCancelledCommand = new MarkOrderAsCancelledCommand("aggregate-order1");
        OrderCancellationRefusedEvent orderCancellationRefusedEvent = new OrderCancellationRefusedEvent("aggregate-order1");

        fixture.given(orderDeliveredEvent)
               .when(markOrderAsCancelledCommand)
               .expectEvents(orderCancellationRefusedEvent)
               .expectNotMarkedDeleted();
    }
}
