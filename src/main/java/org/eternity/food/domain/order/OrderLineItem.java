package org.eternity.food.domain.order;

import lombok.Builder;
import lombok.Getter;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.shop.Menu;
import org.eternity.food.domain.shop.OptionGroup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name="ORDER_LINE_ITEMS")
@Getter
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_LINE_ITEM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="MENU_ID")
    private Menu menu;

    @Column(name="FOOD_NAME")
    private String name;

    @Column(name="FOOD_COUNT")
    private int count;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="ORDER_LINE_ITEM_ID")
    private List<OrderOptionGroup> groups = new ArrayList<>();

    public OrderLineItem(Menu menu, String name, int count, List<OrderOptionGroup> groups) {
        this(null, menu, name, count, groups);
    }

    @Builder
    public OrderLineItem(Long id, Menu menu, String name, int count, List<OrderOptionGroup> groups) {
        this.id = id;
        this.menu = menu;
        this.name = name;
        this.count = count;
        this.groups.addAll(groups);
    }

    OrderLineItem() {
    }

    public Money calculatePrice() {
        return Money.sum(groups, OrderOptionGroup::calculatePrice).times(count);
    }

    public void validate() {
        menu.validateOrder(name, convertToOptionGroups());
    }

    private List<OptionGroup> convertToOptionGroups() {
        return groups.stream().map(OrderOptionGroup::convertToOptionGroup).collect(toList());
    }
}
