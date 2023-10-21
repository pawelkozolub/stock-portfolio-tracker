package org.example.service.portfolio;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Balance;
import org.example.model.Portfolio;
import org.example.model.Transaction;
import org.example.service.BalanceRepository;
import org.example.service.PortfolioRepository;
import org.example.service.TestFixtures;
import org.example.service.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class PortfolioServiceTest {

    private PortfolioRepository portfolioRepositoryMock;
    private TransactionRepository transactionRepositoryMock;
    private BalanceRepository balanceRepositoryMock;
    private PortfolioService portfolioService;


    @BeforeEach
    public void setup() {
        portfolioRepositoryMock = Mockito.mock(PortfolioRepository.class);
        transactionRepositoryMock = Mockito.mock(TransactionRepository.class);
        balanceRepositoryMock = Mockito.mock(BalanceRepository.class);

        portfolioService = new PortfolioService(portfolioRepositoryMock, transactionRepositoryMock, balanceRepositoryMock);
    }

    @Test
    public void givenBalanceLst_whenGetSummary_thenReturnBalanceSummary() {
        Balance balance1 = TestFixtures.balanceForSummary("AAA", 1000.00, 500.00, 100.00);
        Balance balance2 = TestFixtures.balanceForSummary("BBB", 500.00, 100.00, -200.00);
        List<Balance> balanceList = Arrays.asList(balance1, balance2);

        BalanceSummary balanceSummary = portfolioService.getSummary(balanceList);

        assertEquals(TestFixtures.convertToPrice(1500.00), balanceSummary.getInvested());
        assertEquals(TestFixtures.convertToPrice(600.00), balanceSummary.getWithdrawn());
        assertEquals(TestFixtures.convertToPrice(-100.00), balanceSummary.getRealizedProfit());
    }


    @Test
    public void givenPortfolioAndTransaction_whenBuyTransaction_thenCheckPassedArguments() {
        PortfolioService ps = Mockito.mock(PortfolioService.class);
        Portfolio portfolio = TestFixtures.makePortfolioWithId(100L);
        Transaction transaction = TestFixtures.transactionBuy(25L, "XYZ", 1, 25.00);

        Mockito.doAnswer(invocationOnMock -> {
            Portfolio p = invocationOnMock.getArgument(0);
            Transaction t = invocationOnMock.getArgument(1);
            assertEquals(Long.valueOf(100L), p.getId());
            assertEquals(Long.valueOf(25L), t.getId());
            return null;
        }).when(ps).makeBuyPortfolioTransaction(portfolio, transaction);
        ps.makeBuyPortfolioTransaction(portfolio, transaction);
        Mockito.verify(ps).makeBuyPortfolioTransaction(portfolio, transaction);
    }

    @Test
    public void givenPortfolioAndTransaction_whenBuyTransaction_thenUpdateEmptyBalance() {
        PortfolioService ps = Mockito.mock(PortfolioService.class);
        Portfolio portfolio = TestFixtures.makePortfolioWithId(100L);
        Transaction transaction = TestFixtures.transactionBuy(1L, "AAA", 10, 100.00);

        Mockito.doAnswer(invocationOnMock -> {
            Portfolio p = invocationOnMock.getArgument(0);
            Transaction t = invocationOnMock.getArgument(1);

            Balance balance = new Balance(transaction.getStock());
            balance.setPortfolio(portfolio);

            balance.updateByBuyTransaction(transaction);

            assertEquals(100L, balance.getPortfolio().getId());
            assertEquals("AAA", balance.getStock());
            assertEquals(10L, balance.getQuantity());
            assertEquals(TestFixtures.convertToPrice(100.00), balance.getAveragePrice());
            assertEquals(TestFixtures.convertToPrice(1000.00), balance.getInvested());

            return null;
        }).when(ps).makeBuyPortfolioTransaction(portfolio, transaction);

        ps.makeBuyPortfolioTransaction(portfolio, transaction);
        Mockito.verify(ps, Mockito.times(1)).makeBuyPortfolioTransaction(portfolio, transaction);
    }

    @Test
    @Disabled
    public void givenPortfolioAndTransaction_whenBuyTransaction_thenUpdateExistingBalance() {
        PortfolioService ps = Mockito.mock(PortfolioService.class);
        Portfolio portfolio = TestFixtures.makePortfolioWithId(100L);
        String stock = "BBB";
        Transaction transaction1 = TestFixtures.transactionBuy(1L, stock, 10, 100.00);
        Transaction transaction2 = TestFixtures.transactionBuy(2L, stock, 10, 200.00);
        Balance balance = new Balance(stock);
        balance.updateByBuyTransaction(transaction1);

//        Mockito.when(balanceRepositoryMock.findFirstByPortfolioAndStock(portfolio, transaction2.getStock()))
//                .thenReturn(Optional.of(balance));

        Mockito.doAnswer(invocationOnMock -> {
//            Balance balanceInvoked = balanceRepositoryMock
//                    .findFirstByPortfolioAndStock(portfolio, transaction2.getStock())
//                    .orElse(null);
            Balance balanceInvoked = balance;
            if (balanceInvoked == null) {
                log.warn("null balance in test found...");
                return null;
            }

            log.info("{}", balanceInvoked.getPortfolio().getId());
            balanceInvoked.updateByBuyTransaction(transaction2);

            assertEquals(100L, balanceInvoked.getPortfolio().getId());
            assertEquals("AAA", balanceInvoked.getStock());
            assertEquals(20L, balanceInvoked.getQuantity());
            assertEquals(TestFixtures.convertToPrice(150.00), balanceInvoked.getAveragePrice());
            assertEquals(TestFixtures.convertToPrice(3000.00), balanceInvoked.getInvested());

            return null;
        }).when(ps).makeBuyPortfolioTransaction(portfolio, transaction2);
        ps.makeBuyPortfolioTransaction(portfolio, transaction2);
        Mockito.verify(ps).makeBuyPortfolioTransaction(portfolio, transaction2);
    }
}