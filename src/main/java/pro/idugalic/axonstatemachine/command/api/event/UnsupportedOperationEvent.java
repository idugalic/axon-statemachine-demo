package pro.idugalic.axonstatemachine.command.api.event;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
final public class UnsupportedOperationEvent extends AbstractOrderEvent{

    private String orderId;
    private Class<?> commandType;

    public UnsupportedOperationEvent(String aggregateIdentifier, String orderId, Class<?> commandType) {
        super(aggregateIdentifier);
        this.orderId = orderId;
        this.commandType = commandType;
    }
}
