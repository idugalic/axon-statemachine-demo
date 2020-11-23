package pro.idugalic.axonstatemachine.command.api.command;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class CreateOrderCommand {

    public CreateOrderCommand(String orderId, ArrayList<OrderItem> items) {
        this.aggregateIdentifier = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.items = items;
    }

    @TargetAggregateIdentifier
    private String aggregateIdentifier;

    private String orderId;

    private ArrayList<OrderItem> items;
}
