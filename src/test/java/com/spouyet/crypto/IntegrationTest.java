package com.spouyet.crypto;

import com.spouyet.crypto.service.BasicPriceApiResponseParser;
import com.spouyet.crypto.service.BasicPriceApiResponseParserTest;
import com.spouyet.crypto.io.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

public class IntegrationTest {

    @Test
    public void checksTheApplicationRuns() throws IOException {
        final String apiJsonReply = BasicPriceApiResponseParserTest.readFile("src/test/resources/api/reply.json");
        final RestClient restClient = targetUrl -> apiJsonReply;
        final OutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8.name());
        new ApplicationController().run(
                "src/test/resources/portfolio/portfolio.txt",
                Currency.getInstance("CHF"),
                restClient,
                new BasicPriceApiResponseParser(),
                printStream
        );

        final List<String> expectedConsoleOutputs = new ArrayList<>();
        expectedConsoleOutputs.add("BTC value = 98200.50 CHF");
        expectedConsoleOutputs.add("ETH value = 1675.80 CHF");
        expectedConsoleOutputs.add("XRP value = 483.20 CHF");
        expectedConsoleOutputs.add("Total value = 100359.50 CHF");

        final String expectedResult = expectedConsoleOutputs.stream()
                .map(line -> line + System.lineSeparator())
                .collect(Collectors.joining());

        Assertions.assertEquals(expectedResult, outputStream.toString());
    }
}
