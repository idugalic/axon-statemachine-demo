package pro.idugalic.axonstatemachine.command.api.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

import java.util.ArrayList;
import java.util.UUID;

@Getter
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
