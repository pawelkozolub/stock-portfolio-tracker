package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Stock;
import org.example.model.StockPortfolio;
import org.example.service.StockPortfolioRepository;
import org.example.service.StockRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    private final StockPortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;

    public PortfolioController(StockPortfolioRepository portfolioRepository, StockRepository stockRepository) {
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
    }

    @GetMapping
    public String list(Model model) {
        StockPortfolio stockPortfolio = new StockPortfolio();
        model.addAttribute("stockPortfolio", stockPortfolio);
        List<StockPortfolio> stockPortfolioList = portfolioRepository.findAll();
        model.addAttribute("stockPortfolioList", stockPortfolioList);
        return "portfolio/portfolio-index-view";
    }

    @PostMapping
    public String save(StockPortfolio stockPortfolio, BindingResult result) {
        if (result.hasErrors()) {
            return "portfolio/portfolio-index-view";
        }
        portfolioRepository.save(stockPortfolio);
        return "redirect:/portfolio";
    }

    @GetMapping("/{id}")
    public String view(Model model, @PathVariable(name = "id") Long id) {
        StockPortfolio stockPortfolio = portfolioRepository.findById(id).orElse(null);
        model.addAttribute("portfolio", stockPortfolio);
        return "portfolio/portfolio-view";
    }

    @GetMapping("/{portfolioId}/buy")       // portfolioId to be used instead id > Spring confuses it with stock.id
    public String addStock(Model model, @PathVariable(name = "portfolioId") Long id) {
        StockPortfolio stockPortfolio = portfolioRepository.findById(id).orElse(null);
        model.addAttribute("portfolio", stockPortfolio);
        model.addAttribute("stock", new Stock());
        return "portfolio/buy-stock-view";
    }

    @PostMapping("/{portfolioId}/buy")      // portfolioId to be used instead id > Spring confuses it with stock.id
    public String saveStock(Stock stock, BindingResult result, @PathVariable(name = "portfolioId") Long id) {
        if (result.hasErrors()) {
            return "portfolio/buy-stock-view";
        }
        StockPortfolio stockPortfolio = portfolioRepository.findById(id).orElse(null);
        if (stockPortfolio != null) {
            stockRepository.save(stock);                // first persist entity -> stock
            stockPortfolio.getStocks().add(stock);      // then add to list
            portfolioRepository.save(stockPortfolio);   // finally update entity -> stockPortfolio
        }
        return "redirect:/portfolio/" + id;
    }

}
