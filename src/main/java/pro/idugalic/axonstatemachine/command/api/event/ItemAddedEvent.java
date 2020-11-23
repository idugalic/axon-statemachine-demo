package pro.idugalic.axonstatemachine.command.api.event;

import lombok.*;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
final public class ItemAddedEvent extends AbstractOrderEvent{

    private String orderId;
    private OrderItem item;

    public ItemAddedEvent(String aggregateIdentifier, String orderId, OrderItem item) {
        super(aggregateIdentifier);
        this.orderId = orderId;
        this.item = item;
    }
}
