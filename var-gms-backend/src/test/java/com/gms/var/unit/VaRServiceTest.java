package com.gms.var.unit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.gms.var.model.Portfolio;
import com.gms.var.model.Trade;
import com.gms.var.service.VaRService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class VaRServiceTest {

    private final VaRService vaRService = new VaRService();

    @Test
    public void testVaRAtLowConfidenceLevel() {
        // Given
        List<Double> historicalValues = Arrays.asList(100.0, 101.0, 102.0, 103.0, 104.0, 105.0, 106.0, 107.0, 108.0, 109.0);
        double confidenceLevel = 0.01;

        // When
        double var = vaRService.calculateVaR(historicalValues, confidenceLevel);

        // Then
        assertEquals(100.0, var);
    }

    @Test
    public void testVaRAtHighConfidenceLevel() {
        // Given
        List<Double> historicalValues = Arrays.asList(100.0, 101.0, 102.0, 103.0, 104.0, 105.0, 106.0, 107.0, 108.0, 109.0);
        double confidenceLevel = 0.99;

        // When
        double var = vaRService.calculateVaR(historicalValues, confidenceLevel);

        // Then
        assertEquals(109.0, var);
    }
    
    @Test
    public void testCalculateVaR() {
        List<Double> historicalValues = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
        double confidenceLevel = 0.95;
        double expectedVaR = 1.0;
        double result = vaRService.calculateVaR(historicalValues, confidenceLevel);
        assertEquals(expectedVaR, result, 0.001);
    }

    @Test
    public void testCalculatePortfolioVaR() {
        Trade trade1 = new Trade();
        trade1.setId("1");
        trade1.setHistoricalValues(Arrays.asList(1.0, 2.0, 3.0));

        Trade trade2 = new Trade();
        trade2.setId("2");
        trade2.setHistoricalValues(Arrays.asList(4.0, 5.0, 6.0));

        Portfolio portfolio = new Portfolio();
        portfolio.setTrades(Arrays.asList(trade1, trade2));

        double confidenceLevel = 0.95;
        double expectedVaR = 1.0;
        double result = vaRService.calculatePortfolioVaR(portfolio, confidenceLevel);
        assertEquals(expectedVaR, result, 0.001);
    }
}
