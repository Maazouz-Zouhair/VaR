package com.gms.var.dto;

public class VaRResponse {
	
    private double valueAtRisk;

    public VaRResponse(double valueAtRisk) {
        this.valueAtRisk = valueAtRisk;
    }

    public double getValueAtRisk() {
        return valueAtRisk;
    }

    public void setValueAtRisk(double valueAtRisk) {
        this.valueAtRisk = valueAtRisk;
    }
}
