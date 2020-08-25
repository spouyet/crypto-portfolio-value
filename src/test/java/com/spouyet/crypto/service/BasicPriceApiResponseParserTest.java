package com.spouyet.crypto.service;

import com.spouyet.crypto.model.SymbolValueMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

public class BasicPriceApiResponseParserTest {

    private static final Currency TEST_CURRENCY = Currency.getInstance("CHF");

    @Test
    public void checksReplyCanBeParsed() throws IOException {
        final String sampleJsonReply = readFile("src/test/resources/api/reply.json");
        final SymbolValueMap symbolValueMap = new BasicPriceApiResponseParser().parsePrices(sampleJsonReply, TEST_CURRENCY);
        final Set<String> expectedSymbols = new HashSet<>();
        expectedSymbols.add("BTC");
        expectedSymbols.add("ETH");
        expectedSymbols.add("XRP");
        Assertions.assertEquals(expectedSymbols, symbolValueMap.getSymbols());
        Assertions.assertEquals(new BigDecimal("9820.05"), symbolValueMap.get("BTC"));
        Assertions.assertEquals(new BigDecimal("335.16"), symbolValueMap.get("ETH"));
        Assertions.assertEquals(new BigDecimal("0.2416"), symbolValueMap.get("XRP"));
    }

    @Test
    public void checksCurrencyMismatchThrows() throws IOException {
        final String sampleJsonReply = readFile("src/test/resources/api/reply-wrong-currency.json");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> new BasicPriceApiResponseParser().parsePrices(sampleJsonReply, TEST_CURRENCY));
    }

    @Test
    public void checksTypoInJsonThrows() throws IOException {
        final String sampleJsonReply = readFile("src/test/resources/api/reply-with-typo.json");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> new BasicPriceApiResponseParser().parsePrices(sampleJsonReply, TEST_CURRENCY));
    }

    @Test
    public void checksReplyWithSingleSymbolCanBeParsed() throws IOException {
        final String sampleJsonReply = readFile("src/test/resources/api/reply-one-price.json");
        final SymbolValueMap symbolValueMap = new BasicPriceApiResponseParser().parsePrices(sampleJsonReply, TEST_CURRENCY);
        final Set<String> expectedSymbols = new HashSet<>();
        expectedSymbols.add("BTC");
        Assertions.assertEquals(expectedSymbols, symbolValueMap.getSymbols());
        Assertions.assertEquals(new BigDecimal("9820.05"), symbolValueMap.get("BTC"));
    }

    @Test
    public void checksEmptyJsonObjectReplyParsesCorrectly() {
        final String sampleJsonReply = "{}";
        final SymbolValueMap symbolValueMap = new BasicPriceApiResponseParser().parsePrices(sampleJsonReply, TEST_CURRENCY);
        Assertions.assertEquals(0, symbolValueMap.getSymbols().size());
    }

    @Test
    public void checksEmptyReplyThrows() {
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> new BasicPriceApiResponseParser().parsePrices("", TEST_CURRENCY));
    }

    @Test
    public void checksParsingAnInvalidResponseThrows() {
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> new BasicPriceApiResponseParser().parsePrices("}not json{", TEST_CURRENCY));
    }

    public static String readFile(String filePath) throws IOException {
        final Path path = Paths.get(filePath);
        return Files.readAllLines(path).get(0);
    }

}
