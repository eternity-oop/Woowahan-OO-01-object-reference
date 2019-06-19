package org.eternity.food.service.order;

import org.eternity.food.domain.delivery.Delivery;
import org.eternity.food.domain.delivery.DeliveryRepository;
import org.eternity.food.domain.order.Order;
import org.eternity.food.domain.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private DeliveryRepository deliveryRepository;
    private OrderMapper orderMapper;

    public OrderService(OrderMapper orderMapper,
                        OrderRepository orderRepository,
                        DeliveryRepository deliveryRepository) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public void placeOrder(Cart cart) {
        Order order = orderMapper.mapFrom(cart);
        order.place();
        orderRepository.save(order);
    }

    @Transactional
    public void payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.payed();

        Delivery delivery = Delivery.started(order);
        deliveryRepository.save(delivery);
    }

    @Transactional
    public void deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.delivered();

        Delivery delivery = deliveryRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        delivery.complete();
    }
}
