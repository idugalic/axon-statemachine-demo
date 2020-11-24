package pro.idugalic.axonstatemachine.query;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.idugalic.axonstatemachine.command.api.OrderStatus;
import pro.idugalic.axonstatemachine.command.api.event.OrderCanceledEvent;
import pro.idugalic.axonstatemachine.command.api.event.OrderCreatedEvent;
import pro.idugalic.axonstatemachine.command.api.event.OrderDeliveredEvent;
import pro.idugalic.axonstatemachine.command.api.event.OrderPaidEvent;
import pro.idugalic.axonstatemachine.query.api.GetAllStateTransitionAggregateIdsRequest;
import pro.idugalic.axonstatemachine.query.api.GetAllStateTransitionAggregateIdsResponse;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderStateTransitionsProjection {
    private static final Logger log = LoggerFactory.getLogger(OrderStateTransitionsProjection.class);

    /**
     * The purpose of the map is to contain a reference of all state transitions of
     * an order. The "orderId" field remains constant between all state transitions
     * but the aggregateId keeps changing.
     *
     * So we keep a map from an order ID to a map containing the aggregate
     * ID of each constituent state in the finite state machine.
     *
     * E.G.
     *   orderId =  1    retrieves a map of (for example):  NULL -> "uuid"
     *                                                      NEW  -> "uuid"
     *                                                      ... ->
     */
    private final ConcurrentHashMap<String, ConcurrentHashMap<OrderStatus, UUID>> orderStateTransitionsMap
            = new ConcurrentHashMap<>();

    @EventHandler
    void on (OrderCreatedEvent evt) {
        log.info("Handling event: {}", evt);

        // Create the inner map which will track all state transitions for this particular order id.
        ConcurrentHashMap<OrderStatus, UUID> innerMap = new ConcurrentHashMap<>();
        innerMap.put(OrderStatus.NEW, UUID.fromString(evt.getAggregateIdentifier()));

        // Place the inner map as the value for the order id (which remains constant across all state transitions).
        String orderId = evt.getOrderId();
        orderStateTransitionsMap.put(orderId, innerMap);
    }

    @EventHandler
    void on(OrderCanceledEvent evt) {
        log.info("Handling event: {}", evt);

        // Retrieve the existing inner map entry (or throw NPE if it does not exist).
        ConcurrentHashMap<OrderStatus, UUID> innerMap = orderStateTransitionsMap.get(evt.getOrderId());

        // Update the inner map with the aggregate ID with the newly created state.
        innerMap.put(OrderStatus.CANCELED, UUID.fromString(evt.getAggregateIdentifier()));
    }

    @EventHandler
    void on(OrderPaidEvent evt) {
        log.info("Handling event: {}", evt);

        // Retrieve the existing inner map entry (or throw NPE if it does not exist).
        ConcurrentHashMap<OrderStatus, UUID> innerMap = orderStateTransitionsMap.get(evt.getOrderId());

        // Update the inner map with the aggregate ID with the newly created state.
        innerMap.put(OrderStatus.PAID, UUID.fromString(evt.getAggregateIdentifier()));
    }

    @EventHandler
    void on(OrderDeliveredEvent evt) {
        log.info("Handling event: {}", evt);

        // Retrieve the existing inner map entry (or throw NPE if it does not exist).
        ConcurrentHashMap<OrderStatus, UUID> innerMap = orderStateTransitionsMap.get(evt.getOrderId());

        // Update the inner map with the aggregate ID with the newly created state.
        innerMap.put(OrderStatus.DELIVERED, UUID.fromString(evt.getAggregateIdentifier()));
    }


    /**
     * Retrieve all aggregate IDs for the associated states for the orderId specified in the query.
     */
    @QueryHandler
    public GetAllStateTransitionAggregateIdsResponse on(GetAllStateTransitionAggregateIdsRequest query) {
        ConcurrentHashMap<OrderStatus, UUID> innerMap = orderStateTransitionsMap.get(query.getOrderId());
        return new GetAllStateTransitionAggregateIdsResponse(query.getOrderId(), innerMap);
    }
}
