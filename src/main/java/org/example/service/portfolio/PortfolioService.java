package org.example.service.portfolio;

import lombok.NoArgsConstructor;
import org.example.model.Balance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class PortfolioService {
    public BalanceSummary getSummary(List<Balance> balance) {
        return new BalanceSummary(balance);
    };
}
