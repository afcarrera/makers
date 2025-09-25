package ec.com.carrera.makers.loan.controller;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.repository.LoanRepository;
import ec.com.carrera.makers.loan.service.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

@WebFluxTest(LoanController.class)
@ActiveProfiles("loancontroller")
public class LoanControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private LoanService loanService;

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
                .expectStatus().isNotFound();
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
                .expectStatus().isNotFound()
                .expectBody(Loan.class)
                .consumeWith(Assertions::assertNotNull);
    }

    @TestConfiguration(proxyBeanMethods = false)
    @Profile("loancontroller")
    static class TestConfig {
        @Bean
        public LoanRepository loanRepository() {
            return Mockito.mock(LoanRepository.class);
        }
    }
}
