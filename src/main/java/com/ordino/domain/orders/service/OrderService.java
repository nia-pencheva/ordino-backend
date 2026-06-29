package com.ordino.domain.orders.service;

import com.ordino.domain.orders.model.dto.create.CreateOrderRequestDTO;
import com.ordino.domain.orders.model.dto.info.OrderResponseDTO;
import com.ordino.domain.orders.model.dto.orders_page.OrdersPageResponseDTO;
import com.ordino.domain.orders.model.dto.receive.ReceiveOrderRequestDTO;

public interface OrderService {
    OrdersPageResponseDTO getOrders(Integer page, Integer pageSize, String from, String to, String orderStatus, String timeField);

    void createOrder(CreateOrderRequestDTO dto);

    OrderResponseDTO getOrder(Long id);

    void receiveOrder(Long id, ReceiveOrderRequestDTO dto);

    void cancelOrder(Long id);
}
