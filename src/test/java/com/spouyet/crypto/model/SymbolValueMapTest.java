package com.spouyet.crypto.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class SymbolValueMapTest {

    @Test
    public void checksAPortfolioCanBeCreatedWithIntegerQuantities() {
        final SymbolValueMap portfolio = SymbolValueMap.builder()
                .with("BTC", 10)
                .with("ETH", 5)
                .with("XRP", 2000)
                .build();

        Assertions.assertEquals(new BigDecimal(10), portfolio.get("BTC"));
        Assertions.assertEquals(new BigDecimal(5), portfolio.get("ETH"));
        Assertions.assertEquals(new BigDecimal(2000), portfolio.get("XRP"));
    }

    @Test
    public void checksAPortfolioCanBeCreatedWithBigDecimalQuantities() {
        final SymbolValueMap portfolio = SymbolValueMap.builder()
                .with("BTC", new BigDecimal(10))
                .with("ETH", new BigDecimal(5))
                .with("XRP", new BigDecimal(2000))
                .build();

        Assertions.assertEquals(new BigDecimal(10), portfolio.get("BTC"));
        Assertions.assertEquals(new BigDecimal(5), portfolio.get("ETH"));
        Assertions.assertEquals(new BigDecimal(2000), portfolio.get("XRP"));
    }

}
