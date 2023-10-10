package org.example.controller;

import org.example.model.Balance;
import org.example.model.Portfolio;
import org.example.model.Transaction;
import org.example.service.BalanceRepository;
import org.example.service.PortfolioRepository;
import org.example.service.portfolio.PortfolioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;
    private final BalanceRepository balanceRepository;
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioRepository portfolioRepository, BalanceRepository balanceRepository, PortfolioService portfolioService) {
        this.portfolioRepository = portfolioRepository;
        this.balanceRepository = balanceRepository;
        this.portfolioService = portfolioService;
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

    @GetMapping("/delete")
    public String deleteView(@RequestParam Long id, Model model) {
        model.addAttribute("portfolio", portfolioRepository.findById(id).orElse(null));
        return "/portfolio/portfolio-delete-view";
    }

    @PostMapping("/delete")
    public String deleteEntity(@RequestParam Long id) {
        portfolioRepository.findById(id).ifPresent(portfolioRepository::delete);
        return "redirect:/portfolio";
    }

    @GetMapping("/edit")
    public String editView(@RequestParam Long id, Model model) {
        model.addAttribute("portfolio", portfolioRepository.findById(id).orElse(null));
        return "/portfolio/portfolio-edit-view";
    }

    @PostMapping("edit")
    public String editEntity(@Valid Portfolio portfolio, BindingResult result) {
        if (result.hasErrors()) {
            return "/portfolio/portfolio-edit-view";
        }
        portfolioRepository.findById(portfolio.getId()).ifPresent(
                updatePortfolio -> {
                    updatePortfolio.setName(portfolio.getName());
                    updatePortfolio.setDescription(portfolio.getDescription());
                    portfolioRepository.save(updatePortfolio);
                });
        return "redirect:/portfolio";
    }

    @GetMapping("/{id}")
    public String view(Model model, @PathVariable(name = "id") Long id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElse(null);
        List<Balance> balanceList = balanceRepository.findAllByPortfolioOrderByStock(portfolio);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("balanceList", balanceList);
        model.addAttribute("summary", portfolioService.getSummary(balanceList));
        return "portfolio/portfolio-balance-view";
    }

    @GetMapping("/{portfolioId}/buy")       // portfolioId to be used instead id > Spring confuses it with stock.id
    public String buyStockView(Model model, @PathVariable(name = "portfolioId") Long id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElse(null);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("transaction", new Transaction());
        return "portfolio/buy-stock-view";
    }

    @PostMapping("/{portfolioId}/buy")      // portfolioId to be used instead id > Spring confuses it with stock.id
    public String saveBuyStock(@Valid Transaction transaction, BindingResult result, @PathVariable(name = "portfolioId") Long id) {
        if (result.hasErrors()) {
            return "portfolio/buy-stock-view";
        }
        Portfolio portfolio = portfolioRepository.findById(id).orElse(null);
        portfolioService.makeBuyPortfolioTransaction(portfolio, transaction);

        return "redirect:/portfolio/" + id;
    }

    @GetMapping("/{portfolioId}/sell")
    public String sellStockView(Model model, @PathVariable(name = "portfolioId") Long id) {
        Portfolio portfolio = portfolioRepository.findById(id).orElse(null);
        List<Balance> balanceList = balanceRepository.findAllByPortfolioOrderByStock(portfolio);
        if (balanceList.isEmpty()) {
            return "redirect:/portfolio/" + id;
        }
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("balanceList", balanceList);
        return "portfolio/sell-stock-view";
    }

    @PostMapping("/{portfolioId}/sell")
    public String saveSellStock(@Valid Transaction transaction, BindingResult result, @PathVariable(name = "portfolioId") Long id) {
        if (result.hasErrors()) {
            return "portfolio/sell-stock-view";
        }
        Portfolio portfolio = portfolioRepository.findById(id).orElse(null);
        portfolioService.makeSellPortfolioTransaction(portfolio, transaction);
        return "redirect:/portfolio/" + id;
    }
}
