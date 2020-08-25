package com.spouyet.crypto.service;

import com.spouyet.crypto.model.SymbolValueMap;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicPriceApiResponseParser implements PriceApiResponseParser {

    /**
     * <p>Pattern matches <code>"BTC":{"EUR":9820.05}</code> and outputs groups:
     * <ul><li>BTC
     * <li>EUR
     * <li>9820.05
     */
    private static final Pattern PRICE_PATTERN = Pattern.compile("\"([A-Z]+)\":\\{\"([A-Z]+)\":([0-9\\.]+)\\}");

    private static final String COMMA = ",";
    public static final char JSON_OPENING_BRACKET = '{';
    public static final char JSON_CLOSING_BRACKET = '}';

    public SymbolValueMap parsePrices(String jsonReply, Currency requestedCurrency) {
        final SymbolValueMap.Builder symbolPriceValuesBuilder = SymbolValueMap.builder();
        final String innerObject = extractJsonInnerObject(jsonReply);
        if (!innerObject.isEmpty()) {
            final String[] symbolPrices = innerObject.split(COMMA);
            for (String symbolJsonSection : symbolPrices) {
                final Matcher matcher = PRICE_PATTERN.matcher(symbolJsonSection);
                if (matcher.matches()) {
                    final String symbol = matcher.group(1);
                    final String currencyCode = matcher.group(2);
                    final String price = matcher.group(3);
                    final Currency currency = Currency.getInstance(currencyCode);
                    if (requestedCurrency.equals(currency)) {
                        symbolPriceValuesBuilder.with(symbol, new BigDecimal(price));
                    } else {
                        throw new IllegalStateException("Price currency returned by API [" + currency.getCurrencyCode() + "] doesn't match the request [" + requestedCurrency.getCurrencyCode() + "]");
                    }
                } else {
                    throwExceptionForMalformedJson(jsonReply);
                }
            }
        }
        return symbolPriceValuesBuilder.build();
    }

    private String extractJsonInnerObject(String jsonReply) {
        if (jsonReply.length() < 2) {
            throwExceptionForMalformedJson(jsonReply);
        }
        if (jsonReply.charAt(0) != JSON_OPENING_BRACKET || jsonReply.charAt(jsonReply.length() - 1) != JSON_CLOSING_BRACKET) {
            throwExceptionForMalformedJson(jsonReply);
        }
        return jsonReply.substring(1, jsonReply.length() - 1);
    }

    private SymbolValueMap throwExceptionForMalformedJson(String jsonReply) {
        throw new IllegalStateException("Malformed JSON reply [" + jsonReply + "]");
    }


}
