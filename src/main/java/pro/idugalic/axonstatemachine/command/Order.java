package pro.idugalic.axonstatemachine.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PACKAGE)
abstract class Order {

    @AggregateIdentifier
    String aggregateIdentifier;

    String orderId;
    List<OrderItem> items;

    Optional<BigDecimal> getTotal() {
        return items.stream()
                    .map(orderItems -> orderItems.getPrice().multiply(BigDecimal.valueOf(orderItems.getQuantity())))
                    .reduce(BigDecimal::add);
    }
}
