package com.physmo.messagesource;

import com.physmo.message.Msg;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link MessageSource} implementation that fetches data from an HTTP endpoint.
 * This class uses Java's {@link HttpClient} to perform GET requests and wraps
 * the response body and headers into a {@link Msg}.
 */
public class HttpMessageSource implements MessageSource<String> {

    private final String url;
    private final HttpClient httpClient;

    /**
     * Constructs an {@code HttpMessageSource} with the specified URL.
     * A default {@link HttpClient} is used, configured to follow normal redirects.
     *
     * @param url the URL to fetch data from
     */
    public HttpMessageSource(String url) {
        this(url, HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build());
    }

    /**
     * Constructs an {@code HttpMessageSource} with the specified URL and {@link HttpClient}.
     *
     * @param url        the URL to fetch data from
     * @param httpClient the HttpClient to use for requests
     */
    public HttpMessageSource(String url, HttpClient httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    /**
     * Polls the HTTP endpoint and returns a {@link Msg} containing the response body
     * if the request is successful (2xx status code).
     * <p>
     * HTTP response headers are mapped to message headers, and the "http_status_code"
     * is added to the headers map.
     *
     * @return an {@link Optional} containing the message if successful, or empty otherwise
     */
    @Override
    public Optional<Msg<String>> poll() {
        try {
            // Create the HTTP GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Execute the request
            HttpResponse<String> response = sendRequest(request);

            // Check if the response status code indicates success
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                Map<String, Object> headers = new HashMap<>();
                
                // Map HTTP response headers to message headers
                response.headers().map().forEach((k, v) -> {
                    if (v.size() == 1) {
                        headers.put(k, v.get(0));
                    } else {
                        headers.put(k, v);
                    }
                });
                
                // Include the status code in the headers
                headers.put("http_status_code", response.statusCode());

                // Return a Msg with the body and headers
                return Optional.of(new Msg<>(response.body(), headers));
            }
        } catch (Exception e) {
            // In a real application, we might want to log this exception.
        }
        return Optional.empty();
    }

    /**
     * Sends the HTTP request using the internal {@link HttpClient}.
     *
     * @param request the HttpRequest to send
     * @return the HttpResponse from the server
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    protected HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
