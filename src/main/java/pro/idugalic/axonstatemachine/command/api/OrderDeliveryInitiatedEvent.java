package pro.idugalic.axonstatemachine.command.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderDeliveryInitiatedEvent {

    private String aggregateIdentifier;
}
