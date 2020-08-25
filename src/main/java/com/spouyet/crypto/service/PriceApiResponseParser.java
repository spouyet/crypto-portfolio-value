package com.spouyet.crypto.service;

import com.spouyet.crypto.model.SymbolValueMap;

import java.util.Currency;

public interface PriceApiResponseParser {
    // Method for parsing Price API responses into a SymbolValueMap for the pricemulti endpoint
    // The requestCurrency may be used to validate the response price currencies
    SymbolValueMap parsePrices(String jsonReply, Currency requestedCurrency);
}
