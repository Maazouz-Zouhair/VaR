package com.gms.var.model;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class Portfolio {
    
	@NotEmpty
	@Valid
	private List<Trade> trades;
	
	// Getters and Setters
	public List<Trade> getTrades() {
		return trades;
	}

	public void setTrades(List<Trade> trades) {
		this.trades = trades;
	}

    
}
