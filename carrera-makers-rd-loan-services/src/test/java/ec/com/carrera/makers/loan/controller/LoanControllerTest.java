package ec.com.carrera.makers.loan.controller;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.service.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class LoanControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private final LoanService loanService = Mockito.mock(LoanService.class);

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToController(new LoanController(loanService))
                .build();
    }

    @Test
    void testGetAll() {
        Mockito.when(loanService.getLoans()).thenReturn(Mono.just(List.of(new Loan())));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockAuthentication(
                        new UsernamePasswordAuthenticationToken("test", "N/A",
                                List.of(new SimpleGrantedAuthority("ROLE")))
                ))
                .get().uri("/v1/loans")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetLoanById() {

        Mockito.when(loanService.getLoanById("1")).thenReturn(Mono.just(new Loan()));

        webTestClient
                .mutateWith(SecurityMockServerConfigurers.mockAuthentication(
                        new UsernamePasswordAuthenticationToken("test", "N/A",
                                List.of(new SimpleGrantedAuthority("ROLE")))
                ))
                .get().uri("/v1/loans/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Loan.class)
                .consumeWith(Assertions::assertNotNull);
    }


}
