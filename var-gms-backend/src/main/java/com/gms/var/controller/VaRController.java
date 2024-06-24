package com.gms.var.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gms.var.dto.VaRRequest;
import com.gms.var.dto.VaRResponse;
import com.gms.var.model.Portfolio;
import com.gms.var.model.Trade;
import com.gms.var.service.CSVService;
import com.gms.var.service.VaRService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/var")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class VaRController {

    @Autowired
    private VaRService vaRService;

    @Autowired
    private CSVService csvService;

    @PostMapping("/single-trade")
    public VaRResponse calculateSingleTradeVaR(@Valid @ModelAttribute VaRRequest request) throws IOException {
        List<Trade> trades = csvService.parseCSV(request.getFile());
        if (trades.size() != 1) {
            throw new IllegalArgumentException("The CSV file must contain data for exactly one trade.");
        }
        double valueAtRisk = vaRService.calculateVaR(trades.get(0).getHistoricalValues(), request.getConfidenceLevel());
        return new VaRResponse(valueAtRisk);
    }

    @PostMapping("/portfolio")
    public VaRResponse calculatePortfolioVaR(@Valid @ModelAttribute VaRRequest request) throws IOException {
        List<Trade> trades = csvService.parseCSV(request.getFile());
        if (trades.isEmpty()) {
            throw new IllegalArgumentException("The CSV file must contain data for at least one trade.");
        }
        Portfolio portfolio = new Portfolio();
        portfolio.setTrades(trades);
        double valueAtRisk = vaRService.calculatePortfolioVaR(portfolio, request.getConfidenceLevel());
        return new VaRResponse(valueAtRisk);
    }
}
