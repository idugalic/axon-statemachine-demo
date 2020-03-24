package pro.idugalic.axonstatemachine.command.api.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class AddItemToTheOrderCommand {

    @TargetAggregateIdentifier
    private String aggregateIdentifier;
    private String orderId;
    private OrderItem orderItem;
}
