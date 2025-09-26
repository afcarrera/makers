package ec.com.carrera.makers.loan.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Table("loans")
public class Loan {

    @Id
    private String id;

    private BigDecimal amount;

    private Integer term;

    @Column("status_id")
    private String statusId;

    @Column("creation_date")
    private LocalDateTime creationDate;

    @Transient
    private LoanStatus status;

    @Column("user_id")
    private String userId;

    public Loan() {
        super();
        id = String.valueOf(UUID.randomUUID());
    }
}