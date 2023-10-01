package org.example.controller;

import org.example.model.StockPortfolio;
import org.example.service.StockPortfolioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    private final StockPortfolioRepository portfolioRepository;

    public PortfolioController(StockPortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
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
}
