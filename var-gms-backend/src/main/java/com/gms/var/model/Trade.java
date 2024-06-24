package com.gms.var.model;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Trade {
	
	@NotNull
    private String id;
	
	@NotEmpty
    private List<@NotNull Double> historicalValues;
    
	
	public Trade() {
	}
	
	public Trade(String id, List<Double> historicalValues) {
		this.id = id;
		this.historicalValues = historicalValues;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Double> getHistoricalValues() {
		return historicalValues;
	}
	public void setHistoricalValues(List<Double> historicalValues) {
		this.historicalValues = historicalValues;
	}
    
   
}