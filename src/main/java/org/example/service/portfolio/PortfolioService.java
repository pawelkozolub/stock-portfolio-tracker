package org.example.service.portfolio;

import org.example.model.Balance;
import org.example.model.Portfolio;
import org.example.model.Transaction;
import org.example.service.BalanceRepository;
import org.example.service.PortfolioRepository;
import org.example.service.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;

    public PortfolioService(PortfolioRepository portfolioRepository,
                            TransactionRepository transactionRepository,
                            BalanceRepository balanceRepository) {
        this.portfolioRepository = portfolioRepository;
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
    }

    public BalanceSummary getSummary(List<Balance> balance) {
        return new BalanceSummary(balance);
    };

    public void makeBuyPortfolioTransaction(Portfolio portfolio, Transaction transaction) {
        if (portfolio != null) {
            transaction.setCreated(String.valueOf(LocalDateTime.now()));
            transaction.setType("buy");
            transactionRepository.save(transaction);        // first persist Transaction entity
            portfolio.getTransactions().add(transaction);   // then add Transaction entity to list
            portfolioRepository.save(portfolio);            // finally update Portfolio entity

            Balance balance = balanceRepository
                    .findFirstByPortfolioAndStock(portfolio, transaction.getStock())
                    .orElse(null);
            if (balance == null) {
                balance = new Balance(transaction.getStock());
                balance.setPortfolio(portfolio);
            }
            balance.updateByBuyTransaction(transaction);
            balanceRepository.save(balance);
        }
    }

    public void makeSellPortfolioTransaction(Portfolio portfolio, Transaction transaction) {
        if (portfolio != null) {
            Balance balance = balanceRepository
                    .findFirstByPortfolioAndStock(portfolio, transaction.getStock())
                    .orElseThrow(IllegalAccessError::new);
            if (balance.getQuantity() > 0) {
                if (transaction.getQuantity() > balance.getQuantity()) {
                    transaction.setQuantity(balance.getQuantity());
                }
                transaction.setCreated(String.valueOf(LocalDateTime.now()));
                transaction.setType("sell");
                transactionRepository.save(transaction);
                portfolio.getTransactions().add(transaction);
                portfolioRepository.save(portfolio);
                balance.updateBySellTransaction(transaction);
                balanceRepository.save(balance);
            }
        }
    }
}
