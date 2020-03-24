package pro.idugalic.axonstatemachine.command.api.command;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final public class MarkOrderAsCancelledCommand {

    @TargetAggregateIdentifier
    private String aggregateIdentifier;

}
