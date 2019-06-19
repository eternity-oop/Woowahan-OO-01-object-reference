package org.eternity.food.domain.shop;

import lombok.Builder;
import lombok.Data;
import org.eternity.food.domain.generic.money.Money;

@Data
public class Option {
    private String name;
    private Money price;

    @Builder
    public Option(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
