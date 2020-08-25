package com.spouyet.crypto.model;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 * <p>Used to:<ul>
 * <li>store information from the portfolio
 * <li>store the price information for all of the portfolio's symbols
 * <li>store the position values of the portfolio for display
 */
public class SymbolValueMap {
    private final Map<String, BigDecimal> portfolioEntries;

    private SymbolValueMap(Map<String, BigDecimal> portfolioEntries) {
        this.portfolioEntries = portfolioEntries;
    }

    public Set<String> getSymbols() {
        return new LinkedHashSet<>(portfolioEntries.keySet());
    }

    public BigDecimal get(String symbol) {
        return portfolioEntries.get(symbol);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, BigDecimal> portfolioEntries = new LinkedHashMap<>();

        Builder() {}

        public Builder with(String symbol, int value) {
            return with(symbol, new BigDecimal(value));
        }

        public Builder with(String symbol, BigDecimal value) {
            final BigDecimal previousValue = portfolioEntries.put(symbol, value);
            if (previousValue != null) {
                throw new IllegalArgumentException("Symbol [" + symbol + "] already declared with value [" + previousValue + "]");
            }
            return this;
        }

        public SymbolValueMap build() {
            return new SymbolValueMap(new LinkedHashMap<>(portfolioEntries));
        }
    }
}
