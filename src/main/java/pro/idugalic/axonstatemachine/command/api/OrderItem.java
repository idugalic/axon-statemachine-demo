package pro.idugalic.axonstatemachine.command.api;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class OrderItem {

    private String id;
    private String name;
    private Long quantity;
    private BigDecimal price;
}
