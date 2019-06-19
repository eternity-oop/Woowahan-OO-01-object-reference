package org.eternity.food.domain.shop;

import lombok.Builder;
import lombok.Getter;
import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.generic.money.Ratio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="SHOPS")
@Getter
public class Shop {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="SHOP_ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="OPEN")
    private boolean open;

    @Column(name="MIN_ORDER_AMOUNT")
    private Money minOrderAmount;

    @Column(name="COMMISSION_RATE")
    private Ratio commissionRate;

    @Column(name = "COMMISSION")
    private Money commission = Money.ZERO;

    // [조회 전용 양뱡향 연관관계]
    /**
     * MenuBoard 생성을 위한 연관관계
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="SHOP_ID")
    private List<Menu> menus = new ArrayList<>();

    public Shop(String name, boolean open, Money minOrderAmount) {
        this(name, open, minOrderAmount, Ratio.valueOf(0), Money.ZERO);
    }

    public Shop(String name, boolean open, Money minOrderAmount, Ratio commissionRate, Money commission) {
        this(null, name, open, minOrderAmount, commissionRate, commission);
    }

    @Builder
    public Shop(Long id, String name, boolean open, Money minOrderAmount, Ratio commissionRate, Money commission) {
        this.id = id;
        this.name = name;
        this.open = open;
        this.minOrderAmount = minOrderAmount;
        this.commissionRate = commissionRate;
        this.commission = commission;
    }

    Shop() {
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
    }

    public boolean isValidOrderAmount(Money amount) {
        return amount.isGreaterThanOrEqual(minOrderAmount);
    }

    public void open() {
        this.open = true;
    }

    public void close() {
        this.open = true;
    }

    public void modifyCommissionRate(Ratio commissionRate) {
        this.commissionRate = commissionRate;
    }

    public void billCommissionFee(Money price) {
        commission = commission.plus(commissionRate.of(price));
    }
}
