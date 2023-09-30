package org.example.service;

import org.example.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    PriceHistory findFirstByStockOrderByDateDesc(String stock);

    PriceHistory findByStockAndDate(String stock, String date);
}
