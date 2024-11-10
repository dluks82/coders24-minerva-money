package dev.dluks.minervamoney.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "monthly_balances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer month;

    private Integer year;

    @Column(name = "final_balance")
    private BigDecimal finalBalance;

    @Column(name = "closed_at")
    private Instant closedAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
