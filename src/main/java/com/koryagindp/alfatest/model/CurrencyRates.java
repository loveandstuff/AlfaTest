package com.koryagindp.alfatest.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Data
@Getter
@Setter
public class CurrencyRates {
    private String base;
    private Map<String, Double> rates;
}
