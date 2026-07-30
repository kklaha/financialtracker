package org.example;

import java.math.BigDecimal;

public enum Category {
    FOOD(new BigDecimal("40000")),
    TRANSPORT(new BigDecimal("15000")),
    ENTERTAINMENT(new BigDecimal("20000")),
    SALARY(null),
    OTHER(null);

    private final BigDecimal montlyLimit;

    Category(BigDecimal montlyLimit){
        this.montlyLimit=montlyLimit;
    }

    public BigDecimal getMontlyLimit() {
        return montlyLimit;
    }
}
