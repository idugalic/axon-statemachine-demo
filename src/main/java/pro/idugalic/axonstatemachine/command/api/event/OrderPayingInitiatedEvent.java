package pro.idugalic.axonstatemachine.command.api.event;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
final public class OrderPayingInitiatedEvent extends AbstractOrderDeletedEvent {

    public OrderPayingInitiatedEvent(String aggregateIdentifier) {
        super(aggregateIdentifier);
    }
}
