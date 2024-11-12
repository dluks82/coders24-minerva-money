package dev.dluks.minervamoney.entities;

import dev.dluks.minervamoney.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column( nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(length = 255)
    private String description;

    @Column(nullable = false) //data da transação
    private LocalDate date;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false) //momento do registro no banco de dados
    private Instant createdAt;


    @Column(name = "deleted_at") //momento do registro no banco de dados
    private Instant deletedAt;

    @Column(name ="deletion_reason", length = 255)
    private String deletionReason;

    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    public void softDelete(String reason){
        this.deleted = true;
        this.deletionReason = reason;
        this.deletedAt = Instant.now();
    }


}
