package pro.idugalic.axonstatemachine.command.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(exclude = "aggregateIdentifier")
public class OrderDeliveredEvent {

    private String aggregateIdentifier;
    private String orderId;
    private List<OrderItem> items;

}
