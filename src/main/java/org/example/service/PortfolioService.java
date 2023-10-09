package org.example.service;

import lombok.NoArgsConstructor;
import org.example.model.Balance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@NoArgsConstructor
public class PortfolioService {

    public BigDecimal invested(List<Balance> balanceList) {
        BigDecimal invested = BigDecimal.valueOf(0);
        for (Balance balance : balanceList) {
            if (balance.getInvested() != null) {
                invested = invested.add(balance.getInvested());
            }
        }
        return invested;
    }

    public BigDecimal withdrawn(List<Balance> balanceList) {
        BigDecimal withdrawn = BigDecimal.valueOf(0);
        for (Balance balance : balanceList) {
            if (balance.getInvested() != null) {
                withdrawn = withdrawn.add(balance.getWithdrawn());
            }
        }
        return withdrawn;
    }

    public BigDecimal realizedProfit(List<Balance> balanceList) {
        BigDecimal realizedProfit = BigDecimal.valueOf(0);
        for (Balance balance : balanceList) {
            if (balance.getInvested() != null) {
                realizedProfit = realizedProfit.add(balance.getRealizedProfit());
            }
        }
        return realizedProfit;
    }
}
