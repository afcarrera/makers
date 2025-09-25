package ec.com.carrera.makers.loan.dto.enums;

import java.util.Optional;
import java.util.UUID;

public enum LoanStatusEnum {
    PENDIENTE(UUID.fromString("11111111-1111-1111-1111-111111111111")),
    APROBADO(UUID.fromString("22222222-2222-2222-2222-222222222222")),
    RECHAZADO(UUID.fromString("33333333-3333-3333-3333-333333333333"));

    private final UUID id;

    LoanStatusEnum(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
    
    public static Optional<LoanStatusEnum> fromId(UUID id) {
        if (id == null) return Optional.empty();
        for (LoanStatusEnum status : values()) {
            if (status.getId().equals(id)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
