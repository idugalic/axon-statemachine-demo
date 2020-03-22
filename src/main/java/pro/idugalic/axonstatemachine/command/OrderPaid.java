package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsDeliveredCommand;
import pro.idugalic.axonstatemachine.command.api.OrderDeliveryInitiatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.OrderPaidEvent;

import java.util.List;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
final class OrderPaid extends Order {

    OrderPaid(String orderId, List<OrderItem> items) {
        apply(new OrderPaidEvent(UUID.randomUUID().toString(), orderId, items));
    }

    @CommandHandler
    void on(MarkOrderAsDeliveredCommand command) {
        apply(new OrderDeliveryInitiatedEvent(command.getAggregateIdentifier())).andThen(() -> {
            try {
                createNew(OrderDelivered.class, () -> new OrderDelivered(orderId, items));
            } catch (Exception e) {
                log.error("Can not create OrderDelivered aggregate", e);
            }
        });
    }

    @EventSourcingHandler
    void on(OrderPaidEvent event) {
        super.aggregateIdentifier = event.getAggregateIdentifier();
        super.orderId = event.getOrderId();
        super.items = event.getItems();
    }

    @EventSourcingHandler
    void on(OrderDeliveryInitiatedEvent event) {
        markDeleted();
    }
}
