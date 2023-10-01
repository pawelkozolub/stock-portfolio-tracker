package org.example.service;

import org.example.model.StockPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPortfolioRepository extends JpaRepository<StockPortfolio, Long> {
}
