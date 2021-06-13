package com.devtools.plugin.text.translator.impl;

import com.devtools.plugin.exceptions.NoTokenException;
import com.devtools.plugin.exceptions.RequestException;
import com.devtools.plugin.text.translator.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TranslateRequest implements Request {
     private final String token;
     private final static String baseUrl = "https://systran-systran-platform-for-language-processing-v1.p.rapidapi.com/translation/text/translate?source=auto&target=ru&input=";

    /**
     * Constructor from token for API
     * @param token api token
     */
    TranslateRequest(String token) {
        this.token = token;
    }

    /**
     * Constructor from token for API taken from file token.txt
     */
    TranslateRequest() throws NoTokenException {
        this.token = extractToken();
    }

    @Override
    public String send(String textToTranslate) throws RequestException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(encodeUrl(textToTranslate))
                .header("x-rapidapi-key", token)
                .header("x-rapidapi-host", "systran-systran-platform-for-language-processing-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RequestException("Error with sending the request", e);
        }
    }

    /**
     * Exctracts token from token.txt file
     * @return api token
     * @throws NoTokenException - throws, when token for api is not presented
     */
    private String extractToken() throws NoTokenException {
        try {
            return Files.readString(Paths.get("./src/main/java/com/devtools/plugin/text/translator/token.txt"));
        } catch (IOException e) {
            throw new NoTokenException("There is no file with token provided", e);
        }
    }

    /**
     * Constructs URL for request from baseUrl and query(text to translate)
     * @param textToTranslate - query for request
     * @return {@link URI}, ready for HTTPRequest
     */
    private URI encodeUrl(String textToTranslate) {
        String encodedQuery;
        try {
            encodedQuery = URLEncoder.encode(textToTranslate, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Error with encoding URL", ex.getCause());
        }

        return URI.create(baseUrl + encodedQuery);
    }
}
