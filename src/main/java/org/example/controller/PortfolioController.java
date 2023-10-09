package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Balance;
import org.example.model.Portfolio;
import org.example.model.Transaction;
import org.example.service.BalanceRepository;
import org.example.service.PortfolioRepository;
import org.example.service.PortfolioService;
import org.example.service.TransactionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioRepository portfolioRepository, TransactionRepository transactionRepository, BalanceRepository balanceRepository, PortfolioService portfolioService) {
        this.portfolioRepository = portfolioRepository;
        this.transactionRepository = transactionRepository;
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
        model.addAttribute("invested", portfolioService.invested(balanceList));
        model.addAttribute("withdrawn", portfolioService.withdrawn(balanceList));
        model.addAttribute("realizedProfit", portfolioService.realizedProfit(balanceList));
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
        if (portfolio != null) {
            transaction.setCreated(String.valueOf(LocalDateTime.now()));
            transaction.setType("buy");
            transactionRepository.save(transaction);        // first persist Transaction entity
            portfolio.getTransactions().add(transaction);   // then add Transaction entity to list
            portfolioRepository.save(portfolio);            // finally update Portfolio entity

            Balance balance = balanceRepository
                    .findFirstByPortfolioAndStock(portfolio, transaction.getStock())
                    .orElse(null);
            if (balance == null) {
                balance = new Balance(transaction.getStock());
                balance.setPortfolio(portfolio);
            }
            balance.updateByBuyTransaction(transaction);
            balanceRepository.save(balance);
        }
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
        if (portfolio != null) {
            Balance balance = balanceRepository
                    .findFirstByPortfolioAndStock(portfolio, transaction.getStock())
                    .orElseThrow(IllegalAccessError::new);
            if (balance.getQuantity() > 0) {
                if (transaction.getQuantity() > balance.getQuantity()) {
                    transaction.setQuantity(balance.getQuantity());
                }
                transaction.setCreated(String.valueOf(LocalDateTime.now()));
                transaction.setType("sell");
                transactionRepository.save(transaction);
                portfolio.getTransactions().add(transaction);
                portfolioRepository.save(portfolio);
                balance.updateBySellTransaction(transaction);
                balanceRepository.save(balance);
            }
        }
        return "redirect:/portfolio/" + id;
    }
}
