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

    @Test
    public void givenBuyTransaction_whenUpdateByBuyTransaction_thenUpdateInitialValues() {
        Balance bal = new Balance();

        Transaction trans = new Transaction();
        trans.setType(TransactionType.BUY.name());
        trans.setQuantity(10L);
        trans.setPrice(BigDecimal.valueOf(100.00));

        bal.updateByBuyTransaction(trans);
        assertEquals(10L, bal.getQuantity(), "quantity value");
        assertEquals(BigDecimal.valueOf(100.00), bal.getAveragePrice(), "average price value");
        assertEquals(BigDecimal.valueOf(1000.00), bal.getInvested(), "invested value");
    }

    @Test
    public void givenBuyTransaction_whenUpdateByBuyTransaction_thenUpdateExistingValues() {
        Balance bal = new Balance();
        bal.setQuantity(10L);
        bal.setAveragePrice(BigDecimal.valueOf(100.00));
        bal.setInvested(BigDecimal.valueOf(1000.00));

        Transaction trans = new Transaction();
        trans.setType(TransactionType.BUY.name());
        trans.setQuantity(10L);
        trans.setPrice(BigDecimal.valueOf(200.00));

        bal.updateByBuyTransaction(trans);
        assertEquals(20L, bal.getQuantity(), "quantity value");
        assertEquals(BigDecimal.valueOf(150.00), bal.getAveragePrice(), "average price value");
        assertEquals(BigDecimal.valueOf(3000.00), bal.getInvested(), "invested value");
    }

    @Test
    public void givenSellTransaction_whenUpdateBySellTransaction_thenUpdateExistingValues() {
        Balance bal = new Balance();
        bal.setQuantity(20L);
        bal.setAveragePrice(BigDecimal.valueOf(100.00));
        bal.setInvested(BigDecimal.valueOf(2000.00));

        Transaction trans = new Transaction();
        trans.setType(TransactionType.SELL.name());
        trans.setQuantity(10L);
        trans.setPrice(BigDecimal.valueOf(50.00));

        bal.updateBySellTransaction(trans);
        assertEquals(10L, bal.getQuantity(), "quantity value");
        assertEquals(BigDecimal.valueOf(100.00), bal.getAveragePrice(), "average price value");
        assertEquals(BigDecimal.valueOf(2000.00), bal.getInvested(), "invested value");
        assertEquals(BigDecimal.valueOf(500.00), bal.getWithdrawn(), "withdrawn value");
        assertEquals(BigDecimal.valueOf(-500.00), bal.getRealizedProfit(), "realizedProfit value");
    }

    @Test
    public void givenSellAllTransaction_whenUpdateBySellTransaction_thenUpdateExistingValues() {
        Balance bal = new Balance();
        bal.setQuantity(20L);
        bal.setAveragePrice(BigDecimal.valueOf(100.00));
        bal.setInvested(BigDecimal.valueOf(2000.00));

        Transaction trans = new Transaction();
        trans.setType(TransactionType.SELL.name());
        trans.setQuantity(20L);
        trans.setPrice(BigDecimal.valueOf(50.00));

        bal.updateBySellTransaction(trans);
        assertEquals(0L, bal.getQuantity(), "quantity value");
        assertEquals(BigDecimal.valueOf(0.00), bal.getAveragePrice(), "average price value");
        assertEquals(BigDecimal.valueOf(2000.00), bal.getInvested(), "invested value");
        assertEquals(BigDecimal.valueOf(1000.00), bal.getWithdrawn(), "withdrawn value");
        assertEquals(BigDecimal.valueOf(-1000.00), bal.getRealizedProfit(), "realizedProfit value");
    }
}