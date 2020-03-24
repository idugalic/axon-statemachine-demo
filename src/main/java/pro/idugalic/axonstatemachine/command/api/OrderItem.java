package pro.idugalic.axonstatemachine.command.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class OrderItem {

    private String id;
    private String name;
    private Long quantity;
    private BigDecimal price;
}
