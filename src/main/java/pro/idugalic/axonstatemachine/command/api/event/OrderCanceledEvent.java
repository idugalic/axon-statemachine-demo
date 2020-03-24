package pro.idugalic.axonstatemachine.command.api.event;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;

import java.util.ArrayList;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
final public class OrderCanceledEvent extends AbstractOrderCreatedEvent {

    public OrderCanceledEvent(String aggregateIdentifier, String orderId, OrderStatus previousStatus, ArrayList<OrderItem> items) {
        super(aggregateIdentifier, orderId, previousStatus, items);
    }
}
