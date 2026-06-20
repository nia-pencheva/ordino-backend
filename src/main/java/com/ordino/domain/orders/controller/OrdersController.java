package com.ordino.domain.orders.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.orders.model.dto.create.CreateOrderRequestDTO;
import com.ordino.domain.orders.model.dto.info.OrderResponseDTO;
import com.ordino.domain.orders.model.dto.orders_page.OrdersPageResponseDTO;
import com.ordino.domain.orders.model.dto.receive.ReceiveOrderRequestDTO;
import com.ordino.domain.orders.service.OrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/orders")
@Validated
@AllArgsConstructor
public class OrdersController {
    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<OrdersPageResponseDTO> getOrders(
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize,
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String to,
        @RequestParam(required = false) String orderStatus,
        @RequestParam(required = false) String timeField
    ) {
        return ResponseEntity.ok().body(orderService.getOrders(page, pageSize, from, to, orderStatus, timeField));
    }
    
    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderRequestDTO dto) {
        orderService.createOrder(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(orderService.getOrder(id));
    }
    
    @PostMapping("/{id}/receive")
    public ResponseEntity<Void> receiveOrder(@PathVariable @Positive Long id, @Valid @RequestBody ReceiveOrderRequestDTO dto) {
        orderService.receiveOrder(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable @Positive Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
    
}
