package org.example.service;

import org.example.model.Balance;
import org.example.model.Portfolio;
import org.example.model.Transaction;
import org.example.model.TransactionType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestFixtures {

    public static Transaction transactionBuy(Long id, String stock, long qty, double price) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setType(TransactionType.BUY.name());
        transaction.setStock(stock);
        transaction.setQuantity(qty);
        transaction.setPrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP));
        return transaction;
    }

    public static Transaction transactionSell(Long id, String stock, long qty, double price) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setType(TransactionType.SELL.name());
        transaction.setStock(stock);
        transaction.setQuantity(qty);
        transaction.setPrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP));
        return transaction;
    }

    public static Balance balanceForSummary(String stock, double invested, double withdrawn, double realizedProfit) {
        Balance balance = new Balance(stock);
        balance.setInvested(BigDecimal.valueOf(invested).setScale(2, RoundingMode.HALF_UP));
        balance.setWithdrawn(BigDecimal.valueOf(withdrawn).setScale(2, RoundingMode.HALF_UP));
        balance.setRealizedProfit(BigDecimal.valueOf(realizedProfit).setScale(2, RoundingMode.HALF_UP));
        return balance;
    }

    public static BigDecimal convertToPrice(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    public static Portfolio makePortfolioWithId(long id) {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(id);
        return portfolio;
    }
}
