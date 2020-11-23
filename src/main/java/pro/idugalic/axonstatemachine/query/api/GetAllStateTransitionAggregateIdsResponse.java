package pro.idugalic.axonstatemachine.query.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
@AllArgsConstructor
public class GetAllStateTransitionAggregateIdsResponse {

    private String orderId;
    private ConcurrentHashMap<OrderStatus, UUID> stateToAggregateId;
}
