package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;
import pro.idugalic.axonstatemachine.command.api.command.AddItemToTheOrderCommand;
import pro.idugalic.axonstatemachine.command.api.command.CreateOrderCommand;
import pro.idugalic.axonstatemachine.command.api.event.ItemAddedEvent;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsPaidCommand;
import pro.idugalic.axonstatemachine.command.api.event.OrderCancellationInitiatedEvent;
import pro.idugalic.axonstatemachine.command.api.event.OrderCreatedEvent;
import pro.idugalic.axonstatemachine.command.api.event.OrderPayingInitiatedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.createNew;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
final class OrderCreated extends AbstractOrder {

    @CommandHandler
    OrderCreated(CreateOrderCommand command) {
        apply(new OrderCreatedEvent(command.getAggregateIdentifier(), command.getOrderId(), OrderStatus.NULL, command.getItems()));
    }

    @Override
    @CommandHandler
    void on(MarkOrderAsPaidCommand command) {
        apply(new OrderPayingInitiatedEvent(command.getAggregateIdentifier())).andThen(() -> {
            try {
                createNew(OrderPaid.class, () -> new OrderPaid(orderId, OrderStatus.NEW, items));
            } catch (Exception e) {
                log.error("Can't create OrderPaid aggregate", e);
                throw new CommandExecutionException("Can't transition to PAID state", e);
            }
        });
    }

    @Override
    @CommandHandler
    void on(MarkOrderAsCancelledCommand command) {
        apply(new OrderCancellationInitiatedEvent(command.getAggregateIdentifier())).andThen(() -> {
            try {
                createNew(OrderCanceled.class, () -> new OrderCanceled(orderId, OrderStatus.NEW, items));
            } catch (Exception e) {
                log.error("Can not create OrderCanceled aggregate", e);
                throw new CommandExecutionException("Can't transition to CANCELED state", e);
            }
        });
    }

    @Override
    @CommandHandler
    void on(AddItemToTheOrderCommand command) {
        apply(new ItemAddedEvent(command.getAggregateIdentifier(), command.getOrderId(), command.getOrderItem()));
    }

    @EventSourcingHandler
    void on(ItemAddedEvent event) {
       items.add(event.getItem());
    }

}
