package org.example.service;

import org.example.model.Balance;
import org.example.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findFirstByPortfolioAndStock(Portfolio portfolio, @Size(min = 3, max = 3) String stock);

    List<Balance> findAllByPortfolioOrderByStock(Portfolio portfolio);
}
