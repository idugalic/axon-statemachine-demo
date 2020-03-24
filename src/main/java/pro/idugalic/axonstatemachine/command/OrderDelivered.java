package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;
import pro.idugalic.axonstatemachine.command.api.event.OrderDeliveredEvent;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

import java.util.ArrayList;
import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
final class OrderDelivered extends AbstractOrder {

    OrderDelivered(String id, OrderStatus previousStatus, ArrayList<OrderItem> items) {
        apply(new OrderDeliveredEvent(UUID.randomUUID().toString(), id, previousStatus, items));
    }

}
