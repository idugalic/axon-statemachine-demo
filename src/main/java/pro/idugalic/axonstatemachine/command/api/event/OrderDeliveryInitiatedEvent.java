package pro.idugalic.axonstatemachine.command.api.event;

import lombok.*;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
final public class OrderDeliveryInitiatedEvent extends AbstractOrderDeletedEvent {

    public OrderDeliveryInitiatedEvent(String aggregateIdentifier) {
        super(aggregateIdentifier);
    }
}
