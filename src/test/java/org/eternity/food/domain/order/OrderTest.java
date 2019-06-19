package org.eternity.food.domain.order;

import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.generic.money.Ratio;
import org.eternity.food.domain.shop.Shop;
import org.junit.Test;

import java.util.Arrays;

import static org.eternity.food.domain.Fixtures.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrderTest {
    @Test(expected = IllegalArgumentException.class)
    public void 가게_미영업시_주문실패() {
        Order order = anOrder().shop(aShop().open(false).build()).build();

        order.place();
    }

    @Test
    public void 결제완료() {
        Order order = anOrder().status(Order.OrderStatus.ORDERED).build();

        order.payed();

        assertThat(order.getOrderStatus(), is(Order.OrderStatus.PAYED));
    }


    @Test
    public void 배송완료() {
        Shop shop = aShop()
                        .commissionRate(Ratio.valueOf(0.02))
                        .commission(Money.ZERO)
                        .build();

        Order order = anOrder()
                        .shop(shop)
                        .status(Order.OrderStatus.PAYED)
                        .items(Arrays.asList(
                            anOrderLineItem()
                                .count(1)
                                .groups(Arrays.asList(
                                    anOrderOptionGroup()
                                        .options(Arrays.asList(anOrderOption().price(Money.wons(10000)).build())).build()))
                                .build()))
                        .build();

        order.delivered();

        assertThat(order.getOrderStatus(), is(Order.OrderStatus.DELIVERED));
        assertThat(shop.getCommission(), is(Money.wons(200)));
    }
}
