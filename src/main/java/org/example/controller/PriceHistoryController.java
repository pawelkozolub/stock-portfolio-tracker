package org.example.controller;

import org.example.model.PriceHistory;
import org.example.service.PriceHistoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PriceHistoryController {

    private final PriceHistoryRepository priceRepository;

    public PriceHistoryController(PriceHistoryRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @GetMapping("/check/{stock}")
    @ResponseBody
    public String lastPrice(@PathVariable(name = "stock") String stock) {
        PriceHistory sp = priceRepository.findFirstByStockOrderByDateDesc(stock);
        return String.format("%s, %s, close: %s", sp.getStock(), sp.getDate(), sp.getClose());
    }

    @GetMapping("/check/{stock}/{date}")
    @ResponseBody
    public String priceAtDate(@PathVariable(name = "stock") String stock,
                            @PathVariable(name = "date") String date) {
        PriceHistory sp = priceRepository.findByStockAndDate(stock, date);
        return String.format("%s, %s, close: %s", sp.getStock(), sp.getDate(), sp.getClose());
    }

}
