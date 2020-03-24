package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;
import pro.idugalic.axonstatemachine.command.api.event.OrderCanceledEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

import java.util.ArrayList;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class OrderCanceled extends AbstractOrder {

    OrderCanceled(String id, OrderStatus previousStatus, ArrayList<OrderItem> items) {
        apply(new OrderCanceledEvent(UUID.randomUUID().toString(), id, previousStatus, items));
    }

}
