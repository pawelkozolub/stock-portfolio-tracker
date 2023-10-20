package org.example.service.portfolio;

import org.example.model.Balance;
import org.example.service.BalanceRepository;
import org.example.service.PortfolioRepository;
import org.example.service.TestFixtures;
import org.example.service.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

        assertEquals(BigDecimal.valueOf(1500.00).setScale(2, RoundingMode.HALF_UP), balanceSummary.getInvested());
        assertEquals(BigDecimal.valueOf(600.00).setScale(2, RoundingMode.HALF_UP), balanceSummary.getWithdrawn());
        assertEquals(BigDecimal.valueOf(-100.00).setScale(2, RoundingMode.HALF_UP), balanceSummary.getRealizedProfit());
    }

}