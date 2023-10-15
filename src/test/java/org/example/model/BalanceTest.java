package org.example.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    @Test
    public void shouldCalculateValueUsingQuantityAndPrice() {
        Balance balance = new Balance();
        assertEquals(BigDecimal.valueOf(0.00), balance.calculateValue(0L, BigDecimal.valueOf(100.00)));
        assertEquals(BigDecimal.valueOf(0.00), balance.calculateValue(10L, BigDecimal.valueOf(0.00)));
        assertEquals(BigDecimal.valueOf(0.00), balance.calculateValue(0L, BigDecimal.valueOf(0.00)));
        assertEquals(BigDecimal.valueOf(1000.0), balance.calculateValue(10L, BigDecimal.valueOf(100.00)));
    }

}