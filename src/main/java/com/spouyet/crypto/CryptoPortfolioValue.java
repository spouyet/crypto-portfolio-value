package com.spouyet.crypto;

import com.spouyet.crypto.service.BasicPriceApiResponseParser;
import com.spouyet.crypto.io.BasicRestClient;

import java.io.IOException;
import java.util.Currency;

public class CryptoPortfolioValue {

    private static final String PORTFOLIO_FILENAME = "bobs_crypto.txt";
    private static final String DEFAULT_TARGET_CURRENCY_CODE = "EUR";

    public static void main(String[] args) throws IOException {
        final String portfolioFilename = args.length > 0 ? args[0] : PORTFOLIO_FILENAME;
        final Currency currency = Currency.getInstance(args.length > 1 ? args[1] : DEFAULT_TARGET_CURRENCY_CODE);

        new ApplicationController().run(
                portfolioFilename,
                currency,
                new BasicRestClient(),
                new BasicPriceApiResponseParser(),
                System.out);
    }
}
