package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsCancelledCommand;
import pro.idugalic.axonstatemachine.command.api.command.MarkOrderAsDeliveredCommand;
import pro.idugalic.axonstatemachine.command.api.event.OrderCancellationInitiatedEvent;
import pro.idugalic.axonstatemachine.command.api.event.OrderDeliveryInitiatedEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.event.OrderPaidEvent;

import java.util.ArrayList;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.createNew;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
final class OrderPaid extends AbstractOrder {

    OrderPaid(String orderId, OrderStatus previousStatus, ArrayList<OrderItem> items) {
        apply(new OrderPaidEvent(UUID.randomUUID().toString(), orderId, previousStatus, items));
    }

    @Override
    @CommandHandler
    void on(MarkOrderAsDeliveredCommand command) {
        apply(new OrderDeliveryInitiatedEvent(command.getAggregateIdentifier())).andThen(() -> {
            try {
                createNew(OrderDelivered.class, () -> new OrderDelivered(orderId, OrderStatus.PAID, items));
            } catch (Exception e) {
                log.error("Can not create OrderDelivered aggregate", e);
                throw new CommandExecutionException("Can't transition to DELIVERED state", e);
            }
        });
    }

    @Override
    @CommandHandler
    void on(MarkOrderAsCancelledCommand command) {
        apply(new OrderCancellationInitiatedEvent(command.getAggregateIdentifier())).andThen(() -> {
            try {
                createNew(OrderCanceled.class, () -> new OrderCanceled(orderId, OrderStatus.PAID, items));
            } catch (Exception e) {
                log.error("Can not create OrderCanceled aggregate", e);
                throw new CommandExecutionException("Can't transition to CANCELED state", e);
            }
        });
    }
}
