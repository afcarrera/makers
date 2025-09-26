package ec.com.carrera.makers.loan.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer term;

    @Column(name = "status_id", nullable = false)
    private String statusId;

    @Column(name = "creation_date", updatable = false, insertable = false)
    private LocalDateTime creationDate;

    @JoinColumn(name = "user_id", nullable = false)
    private String userId;

    @Transient
    private String previousStatusId;
}
