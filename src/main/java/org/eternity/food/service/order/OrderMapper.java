package org.eternity.food.service.order;

import org.eternity.food.domain.order.Order;
import org.eternity.food.domain.order.OrderLineItem;
import org.eternity.food.domain.order.OrderOption;
import org.eternity.food.domain.order.OrderOptionGroup;
import org.eternity.food.domain.shop.Menu;
import org.eternity.food.domain.shop.MenuRepository;
import org.eternity.food.domain.shop.Shop;
import org.eternity.food.domain.shop.ShopRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class OrderMapper {
    private MenuRepository menuRepository;
    private ShopRepository shopRepository;

    public OrderMapper(MenuRepository menuRepository, ShopRepository shopRepository) {
        this.menuRepository = menuRepository;
        this.shopRepository = shopRepository;
    }

    public Order mapFrom(Cart cart) {
        Shop shop = shopRepository.findById(cart.getShopId())
                                .orElseThrow(IllegalArgumentException::new);

        return new Order(
                        cart.getUserId(),
                        shop,
                        cart.getCartLineItems()
                            .stream()
                            .map(this::toOrderLineItem)
                            .collect(toList()));
    }

    private OrderLineItem toOrderLineItem(Cart.CartLineItem cartLineItem) {
        Menu menu = menuRepository.findById(cartLineItem.getMenuId())
                                .orElseThrow(IllegalArgumentException::new);

        return new OrderLineItem(
                        menu,
                        cartLineItem.getName(),
                        cartLineItem.getCount(),
                        cartLineItem.getGroups()
                                    .stream()
                                    .map(this::toOrderOptionGroup)
                                    .collect(Collectors.toList()));
    }

    private OrderOptionGroup toOrderOptionGroup(Cart.CartOptionGroup cartOptionGroup) {
        return new OrderOptionGroup(
                        cartOptionGroup.getName(),
                        cartOptionGroup.getOptions()
                                    .stream()
                                    .map(this::toOrderOption)
                                    .collect(Collectors.toList()));
    }

    private OrderOption toOrderOption(Cart.CartOption cartOption) {
        return new OrderOption(
                        cartOption.getName(),
                        cartOption.getPrice());
    }
}
