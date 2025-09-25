package ec.com.carrera.makers.loan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loan_status")
public class LoanStatus {

    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

}
