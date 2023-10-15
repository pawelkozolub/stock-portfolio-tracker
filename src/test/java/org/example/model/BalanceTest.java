package org.example.model;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    private Balance balance = new Balance();

    @Test
    public void givenNonZeroQuantityAndPrice_whenCalculateValue_thenNonZeroValue() {
        Long quantity = 10L;
        BigDecimal price = BigDecimal.valueOf(100.00);
        BigDecimal expected = BigDecimal.valueOf(1000.00);
        assertEquals(expected, balance.calculateValue(quantity, price));
    }

    @Test
    public void givenNonZeroQuantityAndZeroPrice_whenCalculateValue_thenZeroValue() {
        Long quantity = 10L;
        BigDecimal price = BigDecimal.valueOf(0.00);
        BigDecimal expected = BigDecimal.valueOf(0.00);
        assertEquals(expected, balance.calculateValue(quantity, price));
    }

    @Test
    public void givenZeroQuantityAndNonZeroPrice_whenCalculateValue_thenZeroValue() {
        Long quantity = 0L;
        BigDecimal price = BigDecimal.valueOf(100.00);
        BigDecimal expected = BigDecimal.valueOf(0.00);
        assertEquals(expected, balance.calculateValue(quantity, price));
    }

    @Test
    public void givenZeroQuantityAndPrice_whenCalculateValue_thenZeroValue() {
        Long quantity = 0L;
        BigDecimal price = BigDecimal.valueOf(0.00);
        BigDecimal expected = BigDecimal.valueOf(0.00);
        assertEquals(expected, balance.calculateValue(quantity, price));
    }

    @Test
    public void givenNonZeroQuantityAndValue_whenCalculateAveragePrice_thenNonZeroPrice() {
        Long quantity = 4L;
        BigDecimal value = BigDecimal.valueOf(10.00);
        BigDecimal expected = BigDecimal.valueOf(2.50);
        assertEquals(expected, balance.calculateAveragePrice(quantity, value));
    }

    @Test
    public void givenNonZeroQuantityAndZeroValue_whenCalculateAveragePrice_thenZeroPrice() {
        Long quantity = 4L;
        BigDecimal value = BigDecimal.valueOf(0.00);
        BigDecimal expected = BigDecimal.valueOf(0.00);
        assertEquals(expected, balance.calculateAveragePrice(quantity, value));
    }

    @Test
    public void givenZeroQuantity_whenCalculateAveragePrice_thenZeroPrice() {
        Long quantity = 0L;
        BigDecimal value = BigDecimal.valueOf(10.00);
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> balance.calculateAveragePrice(quantity, value));
        Truth.assertThat(exception).hasMessageThat().isEqualTo("Average price for quantity = 0 cannot be determined");
    }

}