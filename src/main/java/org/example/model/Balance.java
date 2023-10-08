package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "balances")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 3)
    private String stock;
    @Min(0)
    private long quantity = 0;
    @Column(name = "avg_price", precision = 10, scale = 2)
    @Min(0)
    private BigDecimal averagePrice = BigDecimal.valueOf(0.0);
    @Column(precision = 10, scale = 2)
    @Min(0)
    private BigDecimal invested = BigDecimal.valueOf(0.0);
    @Column(precision = 10, scale = 2)
    @Min(0)
    private BigDecimal withdrawn = BigDecimal.valueOf(0.0);
    @Column(name = "realized_profit", precision = 10, scale = 2)
    private BigDecimal realizedProfit = BigDecimal.valueOf(0.0);
    @ManyToOne
    private Portfolio portfolio;

    public Balance(String stock) {
        this.stock = stock;
    }

    public void updateByBuyTransaction(Transaction transaction) {
        if (transaction.getType().equals("buy")) {
            BigDecimal currentValue = this.averagePrice.multiply(BigDecimal.valueOf(this.quantity));
            BigDecimal transactionValue = transaction.getPrice().multiply(BigDecimal.valueOf(transaction.getQuantity()));
            BigDecimal updatedQuantity = BigDecimal.valueOf(this.quantity + transaction.getQuantity());
            BigDecimal updatedValue = currentValue.add(transactionValue);
            this.quantity += transaction.getQuantity();
            this.averagePrice = updatedValue.divide(updatedQuantity, RoundingMode.HALF_UP);
            this.invested = this.invested.add(transactionValue);
        }
    }

    public void updateBySellTransaction(Transaction transaction) {
        if (transaction.getType().equals("sell")) {
            BigDecimal baseValue = this.averagePrice.multiply(BigDecimal.valueOf(transaction.getQuantity()));
            BigDecimal transactionValue = transaction.getPrice().multiply(BigDecimal.valueOf(transaction.getQuantity()));
            BigDecimal transactionProfit = transactionValue.subtract(baseValue);
            this.quantity -= transaction.getQuantity();
            if (this.quantity == 0) {
                this.averagePrice = BigDecimal.valueOf(0);
            }
            this.realizedProfit = this.realizedProfit.add(transactionProfit);
            this.withdrawn = this.withdrawn.add(transactionValue);
        }
    }
}
