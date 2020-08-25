package com.spouyet.crypto.logic;

import com.spouyet.crypto.model.SymbolValueMap;

import java.math.BigDecimal;

public class PortfolioValueCalculator {

    public SymbolValueMap calculatePortfolioValue(SymbolValueMap symbolValueMap, SymbolValueMap symbolPrices) {
        final SymbolValueMap.Builder builder = SymbolValueMap.builder();
        for (String symbol : symbolValueMap.getSymbols()) {
            final BigDecimal quantity = symbolValueMap.get(symbol);
            final BigDecimal price = symbolPrices.get(symbol);
            final BigDecimal positionPrice = quantity.multiply(price);
            builder.with(symbol, positionPrice);
        }
        return builder.build();
    }
}
