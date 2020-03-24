package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.OrderCancellationRefusedEvent;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PACKAGE)
abstract class NonCancelableOrder extends Order{

    @CommandHandler
    final void on(MarkOrderAsCancelledCommand command) {
        apply(new OrderCancellationRefusedEvent(command.getAggregateIdentifier()));
    }
}
