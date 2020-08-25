package com.spouyet.crypto;

import com.spouyet.crypto.io.PortfolioReader;
import com.spouyet.crypto.io.RestClient;
import com.spouyet.crypto.io.SymbolValueMapRenderer;
import com.spouyet.crypto.logic.PortfolioValueCalculator;
import com.spouyet.crypto.model.SymbolValueMap;
import com.spouyet.crypto.service.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Currency;

public class ApplicationController {

    public void run(String portfolioFilename, Currency currency, RestClient client, PriceApiResponseParser parser, PrintStream out) throws IOException {
        final SymbolValueMap portfolio = new PortfolioReader().readPortfolio(portfolioFilename);
        final SymbolValueMap symbolPrices = new PriceApiService(client, parser).getSymbolPrices(portfolio.getSymbols(), currency);
        final SymbolValueMap portfolioValue = new PortfolioValueCalculator().calculatePortfolioValue(portfolio, symbolPrices);
        new SymbolValueMapRenderer().outputPortfolioValue(portfolioValue, currency, out);
    }
}
