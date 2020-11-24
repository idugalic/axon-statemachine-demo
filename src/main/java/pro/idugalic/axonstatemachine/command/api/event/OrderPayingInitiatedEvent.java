package pro.idugalic.axonstatemachine.command.api.event;

import lombok.*;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
final public class OrderPayingInitiatedEvent extends AbstractOrderDeletedEvent {

    public OrderPayingInitiatedEvent(String aggregateIdentifier) {
        super(aggregateIdentifier);
    }
}
