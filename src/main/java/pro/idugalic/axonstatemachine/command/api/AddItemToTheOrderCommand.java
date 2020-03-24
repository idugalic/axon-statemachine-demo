package pro.idugalic.axonstatemachine.command.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class AddItemToTheOrderCommand {

    @TargetAggregateIdentifier
    private String aggregateIdentifier;
    private String orderId;
    private OrderItem orderItem;
}
