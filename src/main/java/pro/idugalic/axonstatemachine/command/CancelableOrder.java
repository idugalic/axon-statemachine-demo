package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.OrderCancelationInitiatedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.*;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
abstract class CancelableOrder extends Order{

    @CommandHandler
    final void on(MarkOrderAsCancelledCommand command) {
        apply(new OrderCancelationInitiatedEvent(command.getAggregateIdentifier())).andThen(() -> {
            try {
                createNew(OrderCanceled.class, () -> new OrderCanceled(orderId, items));
            } catch (Exception e) {
                log.error("Can not create OrderCanceled aggregate", e);
                throw new CommandExecutionException("Can't transition to CANCELED state", e);
            }
        });
    }

    @EventSourcingHandler
    final void on(OrderCancelationInitiatedEvent event) {
        markDeleted();
    }
}
