package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;
import pro.idugalic.axonstatemachine.command.api.event.AbstractOrderCreatedEvent;
import pro.idugalic.axonstatemachine.command.api.event.AbstractOrderDeletedEvent;
import pro.idugalic.axonstatemachine.command.api.command.AddItemToTheOrderCommand;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsDeliveredCommand;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsPaidCommand;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.event.UnsupportedOperationEvent;

import java.util.ArrayList;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.markDeleted;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PACKAGE)
abstract class AbstractOrder {

    @AggregateIdentifier
    String aggregateIdentifier;
    String orderId;
    OrderStatus previousStatus;
    ArrayList<OrderItem> items;

    @CommandHandler
    void on(AddItemToTheOrderCommand command) {
        apply(new UnsupportedOperationEvent(command.getAggregateIdentifier(), command.getOrderId(), command.getClass()));
    }

    @CommandHandler
    void on(MarkOrderAsPaidCommand command) {
        apply(new UnsupportedOperationEvent(command.getAggregateIdentifier(), null, command.getClass()));
    }

    @CommandHandler
    void on(MarkOrderAsDeliveredCommand command) {
        apply(new UnsupportedOperationEvent(command.getAggregateIdentifier(), null, command.getClass()));
    }

    @CommandHandler
    void on(MarkOrderAsCancelledCommand command) {
        apply(new UnsupportedOperationEvent(command.getAggregateIdentifier(), null, command.getClass()));
    }

    @EventSourcingHandler
    void on(AbstractOrderCreatedEvent event) {
        this.aggregateIdentifier = event.getAggregateIdentifier();
        this.orderId = event.getOrderId();
        this.previousStatus = event.getPreviousStatus();
        this.items = event.getItems();
    }

    @EventSourcingHandler
    void on(AbstractOrderDeletedEvent event) {
        markDeleted();
    }
}
