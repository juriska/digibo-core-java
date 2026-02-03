package com.digibo.core.controller;

import com.digibo.core.dto.request.PaymentSearchRequest;
import com.digibo.core.dto.response.PaymentDetailsResponse;
import com.digibo.core.dto.response.PaymentSearchResponse;
import com.digibo.core.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @Test
    @WithMockUser
    void findPayments_returnsResults() throws Exception {
        PaymentSearchResponse response = PaymentSearchResponse.builder()
                .payments(List.of(
                        Map.of("PAYMENT_ID", "PMT001", "AMOUNT", "1000.00"),
                        Map.of("PAYMENT_ID", "PMT002", "AMOUNT", "2500.00")
                ))
                .pmtClass("STANDARD")
                .build();
        when(paymentService.find(any(PaymentSearchRequest.class))).thenReturn(response);

        String requestBody = """
                {
                    "custId": "CUST001",
                    "pmtClass": "STANDARD"
                }
                """;

        mockMvc.perform(post("/api/payments/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payments").isArray())
                .andExpect(jsonPath("$.payments.length()").value(2))
                .andExpect(jsonPath("$.pmtClass").value("STANDARD"));
    }

    @Test
    @WithMockUser
    void getPaymentDetails_returnsDetails() throws Exception {
        PaymentDetailsResponse details = PaymentDetailsResponse.builder()
                .userId("USER001")
                .userName("John Doe")
                .custName("Acme Corp")
                .benName("Beneficiary Corp")
                .build();
        when(paymentService.getPaymentDetails("PMT001")).thenReturn(details);

        mockMvc.perform(get("/api/payments/PMT001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("USER001"))
                .andExpect(jsonPath("$.userName").value("John Doe"));
    }

    @Test
    @WithMockUser
    void getPaymentDetails_notFound() throws Exception {
        PaymentDetailsResponse emptyDetails = PaymentDetailsResponse.builder().build();
        when(paymentService.getPaymentDetails("UNKNOWN")).thenReturn(emptyDetails);

        mockMvc.perform(get("/api/payments/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void changeTemplateGroup_success() throws Exception {
        doNothing().when(paymentService).changeTemplateGroup(any(), any());

        String requestBody = """
                {
                    "groupId": "GROUP001"
                }
                """;

        mockMvc.perform(post("/api/payments/PMT001/template-group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.paymentId").value("PMT001"))
                .andExpect(jsonPath("$.groupId").value("GROUP001"));
    }

    @Test
    @WithMockUser
    void changeTemplateGroup_missingGroupId() throws Exception {
        mockMvc.perform(post("/api/payments/PMT001/template-group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
