package com.spouyet.crypto.service;

import com.spouyet.crypto.io.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Currency;
import java.util.LinkedHashSet;
import java.util.Set;

public class PriceApiServiceTest {

    @Test
    public void checksPortfolioSymbolsAndRequestedCurrencyGenerateTheRightApiCall() throws IOException {
        final SpyRestClient spyRestClient = new SpyRestClient();
        final PriceApiResponseParser parser = new BasicPriceApiResponseParser();
        final PriceApiService priceApiService = new PriceApiService(spyRestClient, parser);
        final Set<String> symbols = new LinkedHashSet<>();
        symbols.add("BTC");
        symbols.add("ETH");
        symbols.add("XRP");
        priceApiService.getSymbolPrices(symbols, Currency.getInstance("CHF"));
        final String invocationUrl = spyRestClient.getInvocationUrl();
        final String parameters = invocationUrl.substring(invocationUrl.indexOf("?")+1);
        Assertions.assertEquals("fsyms=BTC%2CETH%2CXRP&tsyms=CHF", parameters);
    }

    public static class SpyRestClient implements RestClient {
        private String invocationUrl;
        @Override
        public String invoke(String targetUrl) throws IOException {
            invocationUrl = targetUrl;
            return "{}";
        }
        public String getInvocationUrl() {
            return invocationUrl;
        }
    }

}
