package pro.idugalic.axonstatemachine.query.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetAllStateTransitionAggregateIdsRequest {
    private String orderId;
}
