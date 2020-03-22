package pro.idugalic.axonstatemachine.command;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.junit.jupiter.*;
import pro.idugalic.axonstatemachine.command.api.CreateOrderCommand;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsPaidCommand;
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
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1","order1", items);

        fixture.given()
               .when(createOrderCommand)
               .expectEvents(orderCreatedEvent);
    }

    @Test
    public void orderPaidTest() throws Exception {

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent("aggregate-order1","order1", items);
        MarkOrderAsPaidCommand markOrderAsPaidCommand = new MarkOrderAsPaidCommand("aggregate-order1");
        OrderPayingInitiatedEvent orderPayingInitiatedEvent = new OrderPayingInitiatedEvent("aggregate-order1");
        OrderPaidEvent orderPaidEvent = new OrderPaidEvent("aggregate-order1","order1", items);

        fixture.given(orderCreatedEvent)
               .when(markOrderAsPaidCommand)
               .expectSuccessfulHandlerExecution()
               .expectEvents(orderPayingInitiatedEvent, orderPaidEvent)
               .expectMarkedDeleted();
    }
}
