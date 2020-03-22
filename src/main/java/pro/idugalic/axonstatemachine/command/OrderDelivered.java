package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.OrderDeliveredEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

import java.util.List;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
final class OrderDelivered extends Order {

    OrderDelivered(String id, List<OrderItem> items) {
        apply(new OrderDeliveredEvent(UUID.randomUUID().toString(), id, items));
    }

    @EventSourcingHandler
    void on(OrderDeliveredEvent event) {
        super.aggregateIdentifier = event.getAggregateIdentifier();
        super.orderId = event.getOrderId();
        super.items = event.getItems();
    }
}
