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
        if (transaction.getType().equals(TransactionType.BUY.name())) {
            BigDecimal currentValue = calculateValue(this.quantity, this.averagePrice);
            BigDecimal transactionValue = calculateValue(transaction.getQuantity(), transaction.getPrice());
            this.quantity += transaction.getQuantity();
            this.averagePrice = calculateAveragePrice(this.quantity, currentValue.add(transactionValue));
            this.invested = this.invested.add(transactionValue);
        }
    }

    public void updateBySellTransaction(Transaction transaction) {
        if (transaction.getType().equals(TransactionType.SELL.name())) {
            BigDecimal baseValue = calculateValue(transaction.getQuantity(), this.averagePrice);
            BigDecimal transactionValue = calculateValue(transaction.getQuantity(), transaction.getPrice());
            BigDecimal transactionProfit = transactionValue.subtract(baseValue);
            this.quantity -= transaction.getQuantity();
            if (this.quantity == 0) {
                this.averagePrice = setZeroPrice();
            }
            this.realizedProfit = this.realizedProfit.add(transactionProfit);
            this.withdrawn = this.withdrawn.add(transactionValue);
        }
    }

    public BigDecimal calculateValue(Long quantity, BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal calculateAveragePrice(Long quantity, BigDecimal value) {
        if (quantity == 0) {
            throw new IllegalArgumentException("Average price for quantity = 0 cannot be determined");
        }
        return value.divide(BigDecimal.valueOf(quantity), RoundingMode.HALF_UP);
    }

    public BigDecimal setZeroPrice() {
        return BigDecimal.valueOf(0.0);
    }
}
