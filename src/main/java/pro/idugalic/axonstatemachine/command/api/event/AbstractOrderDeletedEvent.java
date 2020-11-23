package pro.idugalic.axonstatemachine.command.api.event;

import lombok.*;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractOrderDeletedEvent extends AbstractOrderEvent {

    public AbstractOrderDeletedEvent(String aggregateIdentifier) {
        super(aggregateIdentifier);
    }
}
