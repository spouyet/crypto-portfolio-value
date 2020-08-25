package com.spouyet.crypto.service;

import com.spouyet.crypto.io.RestClient;
import com.spouyet.crypto.model.SymbolValueMap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class PriceApiService {

    private static final String API_ENDPOINT = "https://min-api.cryptocompare.com/data/pricemulti";
    public static final String SYMBOL_DELIMITER = ",";

    private final RestClient client;
    private PriceApiResponseParser parser;

    public PriceApiService(RestClient client, PriceApiResponseParser parser) {
        this.client = client;
        this.parser = parser;
    }

    public SymbolValueMap getSymbolPrices(Set<String> symbols, Currency currency) throws IOException {
        final String url = createUrl(symbols, currency);
        final String apiJsonReply;
        apiJsonReply = client.invoke(url);
        final SymbolValueMap symbolPrices = parser.parsePrices(apiJsonReply, currency);
        return symbolPrices;
    }

    private String createUrl(Set<String> symbols, Currency currency) {
        final String fsymsParameter = symbols.stream().collect(Collectors.joining(SYMBOL_DELIMITER));
        try {
            final String utf8EncoderName = StandardCharsets.UTF_8.name();
            return new StringBuilder(API_ENDPOINT)
                    .append("?fsyms=")
                    .append(URLEncoder.encode(fsymsParameter, utf8EncoderName))
                    .append("&tsyms=")
                    .append(URLEncoder.encode(currency.getCurrencyCode(), utf8EncoderName))
                    .toString();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unable to build an API URL from [" + symbols.toString() + "] and currency [" + currency.getCurrencyCode() + "]");
        }
    }
}
