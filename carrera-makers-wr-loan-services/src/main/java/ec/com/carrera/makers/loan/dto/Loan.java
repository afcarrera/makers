package ec.com.carrera.makers.loan.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Loan {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotNull(groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String userId;

    @NotNull(groups = ValidationGroups.Create.class)
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor que 0", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Digits(integer = 12, fraction = 2, message = "El monto debe tener máximo 12 enteros y 2 decimales", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private BigDecimal amount;

    @NotNull(groups = ValidationGroups.Create.class)
    @Min(value = 1, message = "El plazo debe ser mínimo 1", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Max(value = 360, message = "El plazo no puede ser mayor a 360", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Integer term;

    @NotNull(groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String statusId;

    @JsonIgnore
    private String previousStatusId;
}
