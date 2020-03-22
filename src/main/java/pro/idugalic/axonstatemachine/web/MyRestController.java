package pro.idugalic.axonstatemachine.web;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.idugalic.axonstatemachine.command.api.CreateOrderCommand;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsDeliveredCommand;
import pro.idugalic.axonstatemachine.command.api.MarkOrderAsPaidCommand;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api")
public class MyRestController {

    final private CommandGateway commandGateway;

    public MyRestController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping(value = "/orders/new")
    public ResponseEntity<Object> createOrder(@RequestBody final OrderRequest request) {
        commandGateway.send(new CreateOrderCommand(request.getId(), request.getItems()));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/orders/pay")
    public ResponseEntity<Object> payOrder(@RequestBody final PayOrderRequest request) {
        commandGateway.send(new MarkOrderAsPaidCommand(request.getAggregateIdentifier()));
        return ResponseEntity.ok().build();
    }
    @PostMapping(value = "/orders/deliver")
    public ResponseEntity<Object> deliverOrder(@RequestBody final PayOrderRequest request) {
        commandGateway.send(new MarkOrderAsDeliveredCommand(request.getAggregateIdentifier()));
        return ResponseEntity.ok().build();
    }
}
