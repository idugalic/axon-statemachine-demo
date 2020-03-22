package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.CreateOrderCommand;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsPaidCommand;
import pro.idugalic.axonstatemachine.command.api.OrderCreatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderPayingInitiatedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j final class OrderCreated extends Order {

    @CommandHandler
    OrderCreated(CreateOrderCommand command) {
        apply(new OrderCreatedEvent(command.getAggregateIdentifier(), command.getOrderId(), command.getItems()));
    }

    // Some actions/commands that can happen only in this state CREATED. For example: adding items to the order.

    @CommandHandler
    void on(MarkOrderAsPaidCommand command) {
        apply(new OrderPayingInitiatedEvent(command.getAggregateIdentifier())).andThen(() -> {
            try {
                createNew(OrderPaid.class, () -> new OrderPaid(orderId, items));
            } catch (Exception e) {
                log.error("Can not create OrderPaid aggregate", e);
            }
        });
    }

    @EventSourcingHandler
    void on(OrderCreatedEvent event) {
        super.aggregateIdentifier = event.getAggregateIdentifier();
        super.orderId = event.getOrderId();
        super.items = event.getItems();
    }

    @EventSourcingHandler
    void on(OrderPayingInitiatedEvent event) {
        markDeleted();
    }
}
