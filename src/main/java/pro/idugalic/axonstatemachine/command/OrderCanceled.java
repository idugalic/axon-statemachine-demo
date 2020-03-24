package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.OrderCanceledEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

import java.util.List;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class OrderCanceled extends NonCancelableOrder {

    OrderCanceled(String id, List<OrderItem> items) {
        apply(new OrderCanceledEvent(UUID.randomUUID().toString(), id, items));
    }

    @EventSourcingHandler
    void on(OrderCanceledEvent event) {
        super.aggregateIdentifier = event.getAggregateIdentifier();
        super.orderId = event.getOrderId();
        super.items = event.getItems();
    }
}
