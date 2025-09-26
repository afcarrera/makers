package ec.com.carrera.makers.loan.controller;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.entity.LoanStatus;
import ec.com.carrera.makers.loan.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    String domainJson;

    String id;

    Loan loan;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
        domainJson = """
                {
                    "id": "a744542d-a842-48c5-bec6-7a687aa93a15",
                    "userId": "a744542d-a842-48c5-bec6-7a687aa93a15",
                    "amount": 1000,
                    "term": 12,
                    "statusId": "11111111-1111-1111-1111-111111111111"
                }
                """;
        id = "a744542d-a842-48c5-bec6-7a687aa93a15";
        loan = new Loan();
        loan.setId(id);
        loan.setStatusId("11111111-1111-1111-1111-111111111111");
    }

    @Test
    void testCreate() throws Exception {
        when(loanService.create(any(ec.com.carrera.makers.loan.dto.Loan.class))).thenReturn(loan);

        mockMvc.perform(post("/v1/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(domainJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void testUpdate() throws Exception {
        when(loanService.update(any())).thenReturn(loan);

        mockMvc.perform(patch("/v1/loans/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(domainJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

}
