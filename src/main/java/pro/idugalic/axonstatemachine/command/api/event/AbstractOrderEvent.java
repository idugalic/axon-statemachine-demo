package pro.idugalic.axonstatemachine.command.api.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(exclude = "aggregateIdentifier")
public abstract class AbstractOrderEvent {

    private String aggregateIdentifier;

}
