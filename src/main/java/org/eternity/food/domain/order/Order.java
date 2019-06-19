package org.eternity.food.domain.order;

import lombok.Builder;
import lombok.Getter;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.shop.Shop;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ORDERS")
@Getter
public class Order {
    public enum OrderStatus { ORDERED, PAYED, DELIVERED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_ID")
    private Long id;

    @Column(name="USER_ID")
    private Long userId;

    @ManyToOne
    @JoinColumn(name="SHOP_ID")
    private Shop shop;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="ORDER_ID")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Column(name="ORDERED_TIME")
    private LocalDateTime orderedTime;

    @Enumerated(EnumType.STRING)
    @Column(name="STATUS")
    private OrderStatus orderStatus;

    public Order(Long userId, Shop shop, List<OrderLineItem> items) {
        this(userId, shop, items, LocalDateTime.now(), null);
    }

    @Builder
    public Order(Long userId, Shop shop, List<OrderLineItem> items, LocalDateTime orderedTime, OrderStatus status) {
        this.userId = userId;
        this.shop = shop;
        this.orderedTime = orderedTime;
        this.orderStatus = status;
        this.orderLineItems.addAll(items);
    }

    Order() {
    }

    public void place() {
        validate();
        ordered();
    }

    private void validate() {
        if (orderLineItems.isEmpty()) {
            throw new IllegalStateException("주문 항목이 비어 있습니다.");
        }

        if (!shop.isOpen()) {
            throw new IllegalArgumentException("가게가 영업중이 아닙니다.");
        }

        if (!shop.isValidOrderAmount(calculateTotalPrice())) {
            throw new IllegalStateException(String.format("최소 주문 금액 %s 이상을 주문해주세요.", shop.getMinOrderAmount()));
        }

        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.validate();
        }
    }

    private void ordered() {
        this.orderStatus = OrderStatus.ORDERED;
    }

    public void payed() {
        this.orderStatus = OrderStatus.PAYED;
    }

    public void delivered() {
        this.orderStatus = OrderStatus.DELIVERED;
        this.shop.billCommissionFee(calculateTotalPrice());
    }

    private Money calculateTotalPrice() {
        return Money.sum(orderLineItems, OrderLineItem::calculatePrice);
    }
}
