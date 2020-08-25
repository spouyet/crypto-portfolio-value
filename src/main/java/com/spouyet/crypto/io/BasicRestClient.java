package com.spouyet.crypto.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BasicRestClient implements RestClient {

    /** 30s connection timeout in milliseconds
      */
    private static final int CONNECTION_TIMEOUT = 30000;
    public static final String GET_REQUEST_METHOD = "GET";

    public String invoke(String targetUrl) throws IOException {
        final URL url = new URL(targetUrl);
        final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod(GET_REQUEST_METHOD);
        httpConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        httpConnection.connect();

        if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IllegalStateException("Unsuccessful invocation of " + targetUrl);
        }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        final StringBuilder jsonReply = new StringBuilder();
        String output;
        while ((output = reader.readLine()) != null) {
            jsonReply.append(output);
        }
        return jsonReply.toString();
    }

}
