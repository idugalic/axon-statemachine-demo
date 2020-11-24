package pro.idugalic.axonstatemachine.command.api.event;

import lombok.*;

@Getter
@ToString(callSuper = true)
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
