package pro.idugalic.axonstatemachine.command.api.event;

import lombok.*;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
final public class OrderCancellationInitiatedEvent extends AbstractOrderDeletedEvent {

    public OrderCancellationInitiatedEvent(String aggregateIdentifier) {
        super(aggregateIdentifier);
    }
}
