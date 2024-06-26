package com.gms.var.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.gms.var.controller.VaRController;
import com.gms.var.model.Trade;
import com.gms.var.service.CSVService;
import com.gms.var.service.VaRService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VaRController.class)
public class VaRControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VaRService vaRService;

    @MockBean
    private CSVService csvService;

    private MockMultipartFile file;

    @BeforeEach
    public void setUp() throws IOException {
        file = new MockMultipartFile("file", "test.csv", "text/csv", "id,value1,value2\n1,1.0,2.0\n".getBytes());
    }

    @Test
    public void testCalculateSingleTradeVaR() throws Exception {
        List<Trade> mockTrades = Arrays.asList(new Trade("1", Arrays.asList(1.0, 2.0)));
        when(csvService.parseCSV(any())).thenReturn(mockTrades);
        when(vaRService.calculateVaR(any(List.class), eq(0.95))).thenReturn(1.0);

        mockMvc.perform(multipart("/api/var/calculate")
                .file(file)
                .param("confidenceLevel", "0.95"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valueAtRisk\":1.0}"));
    }

    @Test
    public void testCalculateVaR() throws Exception {
        List<Trade> mockTrades = Arrays.asList(
                new Trade("1", Arrays.asList(1.0, 2.0,3.0, 4.0)),
                new Trade("2", Arrays.asList(8.0, 7.0,6.0, 5.0))
        );
        when(csvService.parseCSV(any())).thenReturn(mockTrades);
        when(vaRService.calculatePortfolioVaR(any(), eq(0.95))).thenReturn(1.0);

        mockMvc.perform(multipart("/api/var/calculate")
                .file(file)
                .param("confidenceLevel", "0.95"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valueAtRisk\":1.0}"));
    }
    
    @Test
    public void testCalculateVaRLimitOnePercent() throws Exception {
        List<Trade> mockTrades = Arrays.asList(
                new Trade("1", Arrays.asList(1.0, 2.0,3.0, 4.0)),
                new Trade("2", Arrays.asList(8.0, 7.0,6.0, 5.0))
        );
        when(csvService.parseCSV(any())).thenReturn(mockTrades);
        when(vaRService.calculatePortfolioVaR(any(), eq(0.01))).thenReturn(1.0);

        mockMvc.perform(multipart("/api/var/calculate")
                .file(file)
                .param("confidenceLevel", "0.01"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valueAtRisk\":1.0}"));
    }
    
    @Test
    public void testCalculateVaRLimitNintyNinePercent() throws Exception {
        List<Trade> mockTrades = Arrays.asList(
                new Trade("1", Arrays.asList(1.0, 2.0,3.0, 4.0)),
                new Trade("2", Arrays.asList(8.0, 7.0,6.0, 5.0))
        );
        when(csvService.parseCSV(any())).thenReturn(mockTrades);
        when(vaRService.calculatePortfolioVaR(any(), eq(0.99))).thenReturn(8.0);

        mockMvc.perform(multipart("/api/var/calculate")
                .file(file)
                .param("confidenceLevel", "0.99"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"valueAtRisk\":8.0}"));
    }

    @Test
    public void testCalculateSingleTradeVaRWithInvalidConfidenceLevel() throws Exception {
        mockMvc.perform(multipart("/api/var/calculate")
                .file(file)
                .param("confidenceLevel", "1.5")) // Invalid confidence level
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"confidenceLevel\":\"Confidence level must be between 0.0 and 1.0\"}"));
    }

    @Test
    public void testCalculatePortfolioVaRWithEmptyCSV() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.csv", "text/csv", "id,value1,value2\n".getBytes());

        mockMvc.perform(multipart("/api/var/calculate")
                .file(emptyFile)
                .param("confidenceLevel", "0.95"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The CSV file must contain data for at least one trade."));
    }

    @Test
    public void testCalculateVaRWithZeroConfidenceLevel() throws Exception {
        mockMvc.perform(multipart("/api/var/calculate")
                .file(file)
                .param("confidenceLevel", "0.0")) // Invalid confidence level
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"confidenceLevel\":\"Confidence level must be between 0.0 and 1.0\"}"));
    }

    @Test
    public void testCalculateVaRWithNegativeConfidenceLevel() throws Exception {
        mockMvc.perform(multipart("/api/var/calculate")
                .file(file)
                .param("confidenceLevel", "-0.1")) // Invalid confidence level
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"confidenceLevel\":\"Confidence level must be between 0.0 and 1.0\"}"));
    }

    @Test
    public void testCalculateVaRWithOneConfidenceLevel() throws Exception {
        mockMvc.perform(multipart("/api/var/calculate")
                .file(file)
                .param("confidenceLevel", "1.0")) // Invalid confidence level
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"confidenceLevel\":\"Confidence level must be between 0.0 and 1.0\"}"));
    }
}
