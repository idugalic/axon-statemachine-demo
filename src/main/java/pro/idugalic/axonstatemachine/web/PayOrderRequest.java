package pro.idugalic.axonstatemachine.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
final public class PayOrderRequest implements Serializable {

    private String aggregateIdentifier;

}
