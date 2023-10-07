package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Portfolio;
import org.example.model.Stock;
import org.example.service.PortfolioRepository;
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

    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;

    public PortfolioController(PortfolioRepository portfolioRepository, StockRepository stockRepository) {
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
    }

    @GetMapping
    public String list(Model model) {
        Portfolio portfolio = new Portfolio();
        model.addAttribute("portfolio", portfolio);
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        model.addAttribute("portfolioList", portfolioList);
        return "portfolio/portfolios-list-view";
    }

    @PostMapping
    public String save(Portfolio portfolio, BindingResult result) {
        if (result.hasErrors()) {
            return "portfolio/portfolios-list-view";
        }
        portfolioRepository.save(portfolio);
        return "redirect:/portfolio";
    }

    @GetMapping("/{id}")
    public String view(Model model, @PathVariable(name = "id") Long id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElse(null);
        model.addAttribute("portfolio", portfolio);
        return "portfolio/portfolio-balance-view";
    }

    @GetMapping("/{portfolioId}/buy")       // portfolioId to be used instead id > Spring confuses it with stock.id
    public String addStock(Model model, @PathVariable(name = "portfolioId") Long id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElse(null);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("stock", new Stock());
        return "portfolio/buy-stock-view";
    }

    @PostMapping("/{portfolioId}/buy")      // portfolioId to be used instead id > Spring confuses it with stock.id
    public String saveStock(Stock stock, BindingResult result, @PathVariable(name = "portfolioId") Long id) {
        if (result.hasErrors()) {
            return "portfolio/buy-stock-view";
        }
        Portfolio portfolio = portfolioRepository.findById(id).orElse(null);
        if (portfolio != null) {
            stockRepository.save(stock);                // first persist entity -> stock
            portfolio.getStocks().add(stock);      // then add to list
            portfolioRepository.save(portfolio);   // finally update entity -> stockPortfolio
        }
        return "redirect:/portfolio/" + id;
    }

}
