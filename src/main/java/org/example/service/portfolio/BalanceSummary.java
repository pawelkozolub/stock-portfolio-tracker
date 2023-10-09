package org.example.service.portfolio;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Balance;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class BalanceSummary {
    private BigDecimal invested;
    private BigDecimal withdrawn;
    private BigDecimal realizedProfit;

    public BalanceSummary(List<Balance> balance) {
        this.invested = invested(balance);
        this.withdrawn = withdrawn(balance);
        this.realizedProfit = realizedProfit(balance);
    }

    private BigDecimal invested(List<Balance> balanceList) {
        BigDecimal invested = BigDecimal.valueOf(0);
        for (Balance balance : balanceList) {
            if (balance.getInvested() != null) {
                invested = invested.add(balance.getInvested());
            }
        }
        return invested;
    }

    private BigDecimal withdrawn(List<Balance> balanceList) {
        BigDecimal withdrawn = BigDecimal.valueOf(0);
        for (Balance balance : balanceList) {
            if (balance.getInvested() != null) {
                withdrawn = withdrawn.add(balance.getWithdrawn());
            }
        }
        return withdrawn;
    }

    private BigDecimal realizedProfit(List<Balance> balanceList) {
        BigDecimal realizedProfit = BigDecimal.valueOf(0);
        for (Balance balance : balanceList) {
            if (balance.getInvested() != null) {
                realizedProfit = realizedProfit.add(balance.getRealizedProfit());
            }
        }
        return realizedProfit;
    }
}
