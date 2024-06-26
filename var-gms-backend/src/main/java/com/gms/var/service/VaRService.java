package com.gms.var.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.gms.var.model.Portfolio;

@Service
public class VaRService {

    private static final Logger logger = LoggerFactory.getLogger(VaRService.class);

    public double calculateVaR(List<Double> historicalValues, double confidenceLevel) {
        if (historicalValues.size() <= 2) {
            throw new IllegalArgumentException("PnL vector must have more than 2 points.");
        }

        logger.debug("Calculating VaR with confidence level: " + confidenceLevel);
        Collections.sort(historicalValues);

        int index;
        if (confidenceLevel == 0.01) {
            index = 1; // 1st percentile -> smallest value
        } else if (confidenceLevel == 0.99) {
            index = historicalValues.size(); // 99th percentile -> largest value
        } else {
            index = (int) Math.ceil((1 - confidenceLevel) * historicalValues.size());
        }

        // Adjust the index to be within the valid range
        if (index <= 0) {
            index = 1;
        }
        if (index > historicalValues.size()) {
            index = historicalValues.size();
        }

        double var = historicalValues.get(index - 1);
        logger.debug("Sorted historical values: " + historicalValues);
        logger.debug("Calculated index: " + index);
        logger.debug("VaR calculated: " + var);
        return var;
    }

    public double calculatePortfolioVaR(Portfolio portfolio, double confidenceLevel) {
        if (portfolio.getTrades().isEmpty()) {
            logger.error("The CSV file must contain data for at least one trade.");
            throw new IllegalArgumentException("The CSV file must contain data for at least one trade.");
        }
        List<Double> aggregatedValues = portfolio.getTrades().stream()
                .flatMap(trade -> trade.getHistoricalValues().stream())
                .collect(Collectors.toList());
        logger.debug("All historical values: {}", aggregatedValues);
        return calculateVaR(aggregatedValues, confidenceLevel);
    }
}
