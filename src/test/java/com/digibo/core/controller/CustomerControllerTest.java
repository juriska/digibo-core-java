package com.digibo.core.controller;

import com.digibo.core.dto.response.ChannelResponse;
import com.digibo.core.dto.response.UserResponse;
import com.digibo.core.service.CustomerService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    @WithMockUser
    void customerExists_returnsExists() throws Exception {
        when(customerService.customerExists("CUST001")).thenReturn(1);

        mockMvc.perform(get("/api/customers/exists/CUST001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andExpect(jsonPath("$.customerId").value("CUST001"));
    }

    @Test
    @WithMockUser
    void customerExists_returnsNotExists() throws Exception {
        when(customerService.customerExists("UNKNOWN")).thenReturn(0);

        mockMvc.perform(get("/api/customers/exists/UNKNOWN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(false));
    }

    @Test
    @WithMockUser
    void getUserChannels_returnsList() throws Exception {
        List<Map<String, Object>> channels = List.of(
                Map.of("CHANNEL_ID", 1, "CHANNEL_NAME", "Web Banking"),
                Map.of("CHANNEL_ID", 2, "CHANNEL_NAME", "Mobile Banking")
        );
        when(customerService.loadUserChannels("USER001")).thenReturn(channels);

        mockMvc.perform(get("/api/customers/USER001/channels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser
    void getUser_returnsUserDetails() throws Exception {
        UserResponse user = UserResponse.builder()
                .id(1001L)
                .name("John Doe")
                .email("john@example.com")
                .city("Riga")
                .build();
        when(customerService.loadUser(1001L)).thenReturn(user);

        mockMvc.perform(get("/api/customers/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1001))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser
    void checkLogin_success() throws Exception {
        when(customerService.checkLogin(any(), any(), any(), any())).thenReturn(0);

        String requestBody = """
                {
                    "userId": 1001,
                    "login": "jdoe",
                    "license": "LIC001",
                    "channelId": 1
                }
                """;

        mockMvc.perform(post("/api/customers/login/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.resultCode").value(0));
    }

    @Test
    @WithMockUser
    void checkLicense_valid() throws Exception {
        when(customerService.checkLicense("LIC001")).thenReturn(1);

        mockMvc.perform(get("/api/customers/license/LIC001/check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    @WithMockUser
    void getChannel_returnsChannelInfo() throws Exception {
        ChannelResponse channel = ChannelResponse.builder()
                .wocId("WOC001")
                .custId("CUST001")
                .level(1)
                .cdevType(1)
                .build();
        when(customerService.loadChannel("WOC001", "CUST001")).thenReturn(channel);

        mockMvc.perform(get("/api/customers/channel/WOC001/CUST001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wocId").value("WOC001"))
                .andExpect(jsonPath("$.custId").value("CUST001"));
    }
}
