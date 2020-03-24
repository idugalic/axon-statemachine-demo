package pro.idugalic.axonstatemachine.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.idugalic.axonstatemachine.command.api.OrderItem;

import java.io.Serializable;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
final public class OrderRequest implements Serializable {

    private String id;
    private ArrayList<OrderItem> items;
}
