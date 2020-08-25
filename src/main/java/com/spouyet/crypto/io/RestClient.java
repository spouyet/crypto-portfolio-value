package com.spouyet.crypto.io;

import java.io.IOException;

public interface RestClient {
    // provide an implementation which returns the response from the invocation of targetUrl
    String invoke(String targetUrl) throws IOException;
}
