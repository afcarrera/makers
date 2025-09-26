package ec.com.carrera.makers.loan.controller;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.dto.ValidationGroups;
import ec.com.carrera.makers.loan.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/loans")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class LoanController {

    LoanService domainService;

    @PostMapping
    //@PreAuthorize("hasRole('ROLE_opt-create-domain')")
    public ResponseEntity<Loan> create(@Validated(ValidationGroups.Create.class) @RequestBody @Valid ec.com.carrera.makers.loan.dto.Loan domain) {
        return ResponseEntity.status(HttpStatus.CREATED).body(domainService.create(domain));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Loan> update(@Validated(ValidationGroups.Update.class) @RequestBody @Valid ec.com.carrera.makers.loan.dto.Loan domain, @PathVariable String id) {
        domain.setId(id);
        return ResponseEntity.ok(domainService.update(domain));
    }
}
