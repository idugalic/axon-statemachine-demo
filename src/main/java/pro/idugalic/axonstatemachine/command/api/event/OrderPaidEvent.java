package pro.idugalic.axonstatemachine.command.api.event;

import lombok.*;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;

import java.util.ArrayList;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
final public class OrderPaidEvent extends AbstractOrderCreatedEvent {

    public OrderPaidEvent(String aggregateIdentifier, String orderId, OrderStatus previousStatus, ArrayList<OrderItem> items) {
        super(aggregateIdentifier, orderId, previousStatus, items);
    }
}
