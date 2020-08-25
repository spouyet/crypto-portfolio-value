package com.spouyet.crypto.io;

import com.spouyet.crypto.model.SymbolValueMap;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Currency;

public class SymbolValueMapRenderer {

    public void outputPortfolioValue(SymbolValueMap portfolioValue, Currency currency, PrintStream out) {
        BigDecimal accumulator = BigDecimal.ZERO;
        for (String symbol : portfolioValue.getSymbols()) {
            final BigDecimal positionValue = portfolioValue.get(symbol).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            accumulator = accumulator.add(positionValue);
            out.println(symbol + " value = " + positionValue + " " + currency.getCurrencyCode());
        }
        out.println("Total value = " + accumulator + " " + currency.getCurrencyCode());
    }
}
