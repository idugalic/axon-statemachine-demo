package pro.idugalic.axonstatemachine.command.api.event;

import lombok.*;
import pro.idugalic.axonstatemachine.command.api.OrderItem;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;

import java.util.ArrayList;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractOrderCreatedEvent extends AbstractOrderEvent {

    private String orderId;
    private OrderStatus previousStatus;
    private ArrayList<OrderItem> items;

    public AbstractOrderCreatedEvent(String aggregateIdentifier, String orderId, OrderStatus previousStatus, ArrayList<OrderItem> items) {
        super(aggregateIdentifier);
        this.orderId = orderId;
        this.previousStatus = previousStatus;
        this.items = items;
    }
}
