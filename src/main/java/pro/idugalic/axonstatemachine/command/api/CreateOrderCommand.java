package pro.idugalic.axonstatemachine.command.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOrderCommand {

    public CreateOrderCommand(String orderId, List<OrderItem> items) {
        this.aggregateIdentifier = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.items = items;
    }

    @TargetAggregateIdentifier
    private String aggregateIdentifier;

    private String orderId;

    private List<OrderItem> items;
}
