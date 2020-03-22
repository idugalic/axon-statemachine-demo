package pro.idugalic.axonstatemachine.command.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class OrderItem {

    private String id;
    private String name;
    private Long quantity;
    private BigDecimal price;
}
