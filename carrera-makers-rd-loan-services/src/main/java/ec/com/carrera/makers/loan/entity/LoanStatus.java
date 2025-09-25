package ec.com.carrera.makers.loan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@Table("loans")
public class LoanStatus {

    @Id
    private String id;

    private String name;

    public LoanStatus() {
        super();
        id = String.valueOf(UUID.randomUUID());
    }
}