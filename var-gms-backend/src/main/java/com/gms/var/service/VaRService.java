package com.gms.var.service;


import org.springframework.stereotype.Service;

import com.gms.var.model.Portfolio;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VaRService {

    public double calculateVaR(List<Double> historicalValues, double confidenceLevel) {
        Collections.sort(historicalValues);
        int index = (int) Math.ceil((1 - confidenceLevel) * historicalValues.size());
        return historicalValues.get(index - 1);
    }

    public double calculatePortfolioVaR(Portfolio portfolio, double confidenceLevel) {
        List<Double> combinedHistoricalValues = portfolio.getTrades().stream()
                .flatMap(trade -> trade.getHistoricalValues().stream())
                .collect(Collectors.toList());
        return calculateVaR(combinedHistoricalValues, confidenceLevel);
    }
}
