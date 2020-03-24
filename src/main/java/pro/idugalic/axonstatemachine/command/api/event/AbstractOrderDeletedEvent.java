package pro.idugalic.axonstatemachine.command.api.event;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractOrderDeletedEvent extends AbstractOrderEvent {

    public AbstractOrderDeletedEvent(String aggregateIdentifier) {
        super(aggregateIdentifier);
    }
}
