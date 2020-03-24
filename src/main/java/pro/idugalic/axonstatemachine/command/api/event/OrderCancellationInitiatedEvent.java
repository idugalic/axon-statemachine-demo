package pro.idugalic.axonstatemachine.command.api.event;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
final public class OrderCancellationInitiatedEvent extends AbstractOrderDeletedEvent {

    public OrderCancellationInitiatedEvent(String aggregateIdentifier) {
        super(aggregateIdentifier);
    }
}
