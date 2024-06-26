package com.gms.var.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gms.var.model.Portfolio;

@Service
public class VaRService {

	 private static final Logger logger = LoggerFactory.getLogger(VaRService.class);

	    public double calculateVaR(List<Double> aggregatedHistoricalValues, double confidenceLevel) {
	        if (aggregatedHistoricalValues.size() <= 2) {
	            throw new IllegalArgumentException("PnL vector must have more than 2 points.");
	        }

	        logger.debug("Calculating VaR with confidence level: " + confidenceLevel);
	        Collections.sort(aggregatedHistoricalValues);

	        int index;
	        if (confidenceLevel == 0.01) {
	            index = 1; // 1st percentile -> smallest value
	        } else if (confidenceLevel == 0.99) {
	            index = aggregatedHistoricalValues.size(); // 99th percentile -> largest value
	        } else {
	            index = (int) Math.ceil((1 - confidenceLevel) * aggregatedHistoricalValues.size());
	        }

	        // Adjust the index to be within the valid range
	        if (index <= 0) {
	            index = 1;
	        }
	        if (index > aggregatedHistoricalValues.size()) {
	            index = aggregatedHistoricalValues.size();
	        }

	        double var = aggregatedHistoricalValues.get(index - 1);
	        logger.debug("Sorted aggregated Historical Values: " + aggregatedHistoricalValues);
	        logger.debug("Calculated index: " + index);
	        logger.debug("VaR calculated: " + var);
	        return var;
	    }

    public double calculatePortfolioVaR(Portfolio portfolio, double confidenceLevel) throws IOException {

        // Find the maximum length of historical values
        int maxNumValues = portfolio.getTrades().stream()
                .mapToInt(trade -> trade.getHistoricalValues().size())
                .max()
                .orElse(0);
        
        logger.debug("maximum vector length: " + maxNumValues);
		// Aggregate values by summing them at each point in time, padding with zeros if necessary
        List<Double> aggregatedHistoricalValues = IntStream.range(0, maxNumValues)
                .mapToObj(i -> portfolio.getTrades().stream()
                        .mapToDouble(trade -> trade.getHistoricalValues().size() > i ? trade.getHistoricalValues().get(i) : 0.0)
                        .sum())
                .collect(Collectors.toList());
        logger.debug("aggregated Historical Values: " + aggregatedHistoricalValues);
        return calculateVaR(aggregatedHistoricalValues, confidenceLevel);
    }
}
