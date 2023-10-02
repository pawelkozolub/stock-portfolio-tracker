package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Size(min = 3, max = 3)
    private String stock;
    @Min(0)
    private long quantity;
    @Column(name = "buy_price", precision = 10, scale = 2)
    @Min(0)
    private BigDecimal buyPrice;
    @Column(name = "buy_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String buyDate;
}
