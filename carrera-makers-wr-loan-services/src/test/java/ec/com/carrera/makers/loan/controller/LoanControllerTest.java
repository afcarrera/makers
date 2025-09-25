package ec.com.carrera.makers.loan.controller;

import ec.com.carrera.makers.loan.entity.Loan;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService domainService;

    @InjectMocks
    private LoanController domainController;

    String domainJson;

    String id;

    Loan domain;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(domainController).build();
        domainJson = """
                {
                    "id": "a744542d-a842-48c5-bec6-7a687aa93a15",
                    "userId": "a744542d-a842-48c5-bec6-7a687aa93a15",
                    "amount": 1000,
                    "term": 12
                }
                """;
        id = "a744542d-a842-48c5-bec6-7a687aa93a15";
        domain = new Loan();
        domain.setId(id);
    }

    @Test
    void testCreate() throws Exception {
        when(domainService.create(any(ec.com.carrera.makers.loan.dto.Loan.class))).thenReturn(domain);

        mockMvc.perform(post("/v1/domains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(domainJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void testUpdate() throws Exception {
        when(domainService.update(any(ec.com.carrera.makers.loan.dto.Loan.class))).thenReturn(domain);

        mockMvc.perform(patch("/v1/domains/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(domainJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

}
