package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.AddItemToTheOrderCommand;
import pro.idugalic.axonstatemachine.command.api.CreateOrderCommand;
import pro.idugalic.axonstatemachine.command.api.ItemAddedEvent;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsPaidCommand;
import pro.idugalic.axonstatemachine.command.api.OrderCreatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderPayingInitiatedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
final class OrderCreated extends CancelableOrder {

    /**
     * Creational command handler/constructor for {@link OrderCreated} aggregate.
     *
     * Pleae note that, having `creational` command handlers of the same command name on different aggregates in the same hierarchy is forbidden, since Axon cannot derive which one to invoke.
     *
     * @param command
     */
    @CommandHandler
    OrderCreated(CreateOrderCommand command) {
        apply(new OrderCreatedEvent(command.getAggregateIdentifier(), command.getOrderId(), command.getItems()));
    }

    /**
     * Add item to the order.
     * It can happen only in this state (CREATED).
     *
     * @param command
     */
    @CommandHandler
    void on(AddItemToTheOrderCommand command) {
        apply(new ItemAddedEvent(command.getAggregateIdentifier(), command.getOrderId(), command.getOrderItem()));
    }

    /**
     * Mark order as paid.
     * It can happen only in this state (CREATED).
     * It will trigger transition to the next state (PAID)
     *
     * Transition is performed in two steps:
     * 1.) by applying the {@link OrderPayingInitiatedEvent} which will mark current aggregate instance (state) as deleted
     * 2.) by creating/spawning a new aggregate instance (state) of type {@link OrderPaid}
     *
     * @param command
     */
    @CommandHandler
    void on(MarkOrderAsPaidCommand command) {
        apply(new OrderPayingInitiatedEvent(command.getAggregateIdentifier())).andThen(() -> {
            try {
                createNew(OrderPaid.class, () -> new OrderPaid(orderId, items));
            } catch (Exception e) {
                log.error("Can't create OrderPaid aggregate", e);
                throw new CommandExecutionException("Can't transition to PAID state", e);
            }
        });
    }

    @EventSourcingHandler
    void on(OrderCreatedEvent event) {
        super.aggregateIdentifier = event.getAggregateIdentifier();
        super.orderId = event.getOrderId();
        super.items = event.getItems();
    }

    /**
     * Mark current aggregate instance (state) as deleted
     *
     * @param event
     */
    @EventSourcingHandler
    void on(OrderPayingInitiatedEvent event) {
        markDeleted();
    }
}
