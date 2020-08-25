package com.spouyet.crypto.io;

import com.spouyet.crypto.io.PortfolioReader;
import com.spouyet.crypto.model.SymbolValueMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class PortfolioReaderTest {

    private static final String TEST_PORTFOLIO_DIR = "src/test/resources/portfolio";

    @Test
    public void checksReaderNotifiesOfMissingFile() {
        Assertions.assertThrows(
                IOException.class,
                () -> new PortfolioReader().readPortfolio("invalid_path"));
    }

    @Test
    public void checksReaderNotifiesOfInvalidPortfolioEntryFormat() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new PortfolioReader().readPortfolio(TEST_PORTFOLIO_DIR + "/portfolio_with_invalid_entry.txt"));
    }

    @Test
    public void checksReaderNotifiesOfInvalidPortfolioSymbol() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new PortfolioReader().readPortfolio(TEST_PORTFOLIO_DIR + "/portfolio_with_invalid_symbol.txt"));
    }

    @Test
    public void checksReaderNotifiesOfInvalidPortfolioQuantity() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new PortfolioReader().readPortfolio(TEST_PORTFOLIO_DIR + "/portfolio_with_invalid_quantity.txt"));
    }

    @Test
    public void checksReaderNotifiesOfRedundantSymbolDefinition() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new PortfolioReader().readPortfolio(TEST_PORTFOLIO_DIR + "/portfolio_with_redundant_symbol.txt"));
    }

    @Test
    public void checksPortfolioFileContentIsReadCorrectly() throws IOException {
        final SymbolValueMap symbolValueMap = new PortfolioReader().readPortfolio(TEST_PORTFOLIO_DIR + "/portfolio.txt");
        final Set<String> portfolioSymbols = symbolValueMap.getSymbols();

        Assertions.assertEquals(3, portfolioSymbols.size());
        final Set<String> expectedSymbols = new HashSet<String>();
        expectedSymbols.add("BTC");
        expectedSymbols.add("ETH");
        expectedSymbols.add("XRP");
        Assertions.assertTrue(expectedSymbols.equals(portfolioSymbols));

        Assertions.assertEquals(new BigDecimal(10), symbolValueMap.get("BTC"));
        Assertions.assertEquals(new BigDecimal(5), symbolValueMap.get("ETH"));
        Assertions.assertEquals(new BigDecimal(2000), symbolValueMap.get("XRP"));
    }
}
