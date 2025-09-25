package ec.com.carrera.makers.loan.controller;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequestMapping("/v1/loans")
@Slf4j
public class LoanController {

    LoanService domainService;

    @GetMapping
    //@PreAuthorize("hasRole('ROLE')")
    public Mono<List<Loan>> getAll() {
        return domainService.getLoans();
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE')")
    public Mono<ResponseEntity<Loan>> getLoanById(@PathVariable String id) {
        return domainService.getLoanById(id).map(ResponseEntity::ok);
    }
}
