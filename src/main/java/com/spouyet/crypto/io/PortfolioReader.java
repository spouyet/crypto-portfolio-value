package com.spouyet.crypto.io;

import com.spouyet.crypto.model.SymbolValueMap;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PortfolioReader {

    private static final Pattern SYMBOL_PATTERN = Pattern.compile("[A-Z]+");
    private static final String PORTFOLIO_ENTRY_SEPARATOR = "=";

    public SymbolValueMap readPortfolio(String portfolioPath) throws IOException {
        final Path path = Paths.get(portfolioPath);
        try (final Stream<String> portfolioEntryStream = Files.lines(path, StandardCharsets.UTF_8)) {
            final SymbolValueMap.Builder builder = SymbolValueMap.builder();
            portfolioEntryStream
                    .filter(this::lineNotEmptyPredicate)
                    .forEach(line -> parseAndRegisterPortfolioEntry(builder, line));
            return builder.build();
        }
    }

    private boolean lineNotEmptyPredicate(String line) {
        return !line.trim().isEmpty();
    }

    private void parseAndRegisterPortfolioEntry(SymbolValueMap.Builder portfolioBuilder, String line) {
        final String[] entryValues = line.split(PORTFOLIO_ENTRY_SEPARATOR);
        if (entryValues.length != 2) {
            throw new IllegalArgumentException("Invalid portfolio entry [" + line + "]. Expected format is SYMBOL=QUANTITY");
        }
        final String symbol = entryValues[0];
        final String quantityString = entryValues[1];

        verifySymbol(symbol);
        final BigDecimal quantity = parseQuantity(quantityString);

        portfolioBuilder.with(symbol, quantity);
    }

    private void verifySymbol(String symbol) {
        final Matcher matcher = SYMBOL_PATTERN.matcher(symbol);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Symbol should only consist of capital letters [" + symbol + "]");
        }
    }

    private BigDecimal parseQuantity(String quantityString) {
        try {
            return new BigDecimal(quantityString);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Unable to parse quantity [" + quantityString + "]", nfe);
        }
    }

}
