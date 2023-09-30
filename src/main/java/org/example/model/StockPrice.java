package org.example.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock_prices")
@NoArgsConstructor
@AllArgsConstructor
public class StockPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 3)
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;
    @Column(precision = 10, scale = 2)
    private BigDecimal open;
    @Column(precision = 10, scale = 2)
    private BigDecimal high;
    @Column(precision = 10, scale = 2)
    private BigDecimal low;
    @Column(precision = 10, scale = 2)
    private BigDecimal close;
    private Long volume;
}
