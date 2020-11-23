package pro.idugalic.axonstatemachine.command.api.event;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(exclude = "aggregateIdentifier")
public abstract class AbstractOrderEvent {

    private String aggregateIdentifier;

}
