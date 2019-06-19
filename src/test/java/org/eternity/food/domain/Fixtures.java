package org.eternity.food.domain;

import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.generic.money.Ratio;
import org.eternity.food.domain.order.Order;
import org.eternity.food.domain.order.Order.OrderBuilder;
import org.eternity.food.domain.order.OrderLineItem;
import org.eternity.food.domain.order.OrderLineItem.OrderLineItemBuilder;
import org.eternity.food.domain.order.OrderOption;
import org.eternity.food.domain.order.OrderOption.OrderOptionBuilder;
import org.eternity.food.domain.order.OrderOptionGroup;
import org.eternity.food.domain.order.OrderOptionGroup.OrderOptionGroupBuilder;
import org.eternity.food.domain.shop.*;
import org.eternity.food.domain.shop.Menu.MenuBuilder;
import org.eternity.food.domain.shop.Option.OptionBuilder;
import org.eternity.food.domain.shop.OptionGroup.OptionGroupBuilder;
import org.eternity.food.domain.shop.OptionGroupSpecification.OptionGroupSpecificationBuilder;
import org.eternity.food.domain.shop.OptionSpecification.OptionSpecificationBuilder;
import org.eternity.food.domain.shop.Shop.ShopBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;

import static java.time.LocalDateTime.of;

public class Fixtures {
    public static ShopBuilder aShop() {
        return Shop.builder()
                .name("오겹돼지")
                .commissionRate(Ratio.valueOf(0.01))
                .open(true)
                .minOrderAmount(Money.wons(13000))
                .commission(Money.ZERO);
    }

    public static MenuBuilder aMenu() {
        return Menu.builder()
                .shop(aShop().build())
                .name("삼겹살 1인세트")
                .description("삼겹살 + 야채세트 + 김치찌개")
                .basic(anOptionGroupSpec()
                            .name("기본")
                            .options(Arrays.asList(anOptionSpec().name("소(250g)").price(Money.wons(12000)).build()))
                            .build())
                .additives(Arrays.asList(
                        anOptionGroupSpec()
                            .basic(false)
                            .name("맛선택")
                            .options(Arrays.asList(anOptionSpec().name("매콤 맛").price(Money.wons(1000)).build()))
                            .build()));
    }

    public static OptionGroupSpecificationBuilder anOptionGroupSpec() {
        return OptionGroupSpecification.builder()
                .basic(true)
                .exclusive(true)
                .name("기본")
                .options(Arrays.asList(anOptionSpec().build()));
    }

    public static OptionSpecificationBuilder anOptionSpec() {
        return OptionSpecification.builder()
                .name("소(250g)")
                .price(Money.wons(12000));
    }

    public static OptionGroupBuilder anOptionGroup() {
        return OptionGroup.builder()
                .name("기본")
                .options(Arrays.asList(anOption().build()));
    }

    public static OptionBuilder anOption() {
        return Option.builder()
                .name("소(250g)")
                .price(Money.wons(12000));
    }

    public static OrderBuilder anOrder() {
        return Order.builder()
                .userId(1L)
                .shop(aShop().build())
                .status(Order.OrderStatus.ORDERED)
                .orderedTime(LocalDateTime.of(2020, 1, 1, 12, 0))
                .items(Arrays.asList(anOrderLineItem().build()));
    }

    public static OrderLineItemBuilder anOrderLineItem() {
        return OrderLineItem.builder()
                .menu(aMenu().build())
                .name("삼겹살 1인세트")
                .count(1)
                .groups(Arrays.asList(
                    anOrderOptionGroup()
                            .name("기본")
                            .options(Arrays.asList(anOrderOption().name("소(250g)").price(Money.wons(12000)).build()))
                            .build(),
                    anOrderOptionGroup()
                            .name("맛선택")
                            .options(Arrays.asList(anOrderOption().name("매콤 맛").price(Money.wons(1000)).build()))
                            .build()));
    }

    public static OrderOptionGroupBuilder anOrderOptionGroup() {
        return OrderOptionGroup.builder()
                .name("기본")
                .options(Arrays.asList(anOrderOption().build()));
    }

    public static OrderOptionBuilder anOrderOption() {
        return OrderOption.builder()
                .name("소(250g)")
                .price(Money.wons(12000));
    }
}