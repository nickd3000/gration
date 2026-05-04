package com.physmo.messagesource

import spock.lang.Specification
import java.net.http.HttpResponse
import java.net.http.HttpHeaders
import java.net.http.HttpRequest

class HttpMessageSourceSpec extends Specification {
    def "should return Msg with body when HTTP request is successful"() {
        given:
        def url = "http://example.com"
        def mockResponse = Mock(HttpResponse)
        def headersMap = ["Content-Type": ["text/plain"]]
        def mockHeaders = HttpHeaders.of(headersMap, { k, v -> true })
        
        mockResponse.statusCode() >> 200
        mockResponse.body() >> "Hello World"
        mockResponse.headers() >> mockHeaders
        
        def source = new HttpMessageSource(url, null) {
            @Override
            protected HttpResponse<String> sendRequest(HttpRequest request) {
                return mockResponse
            }
        }

        when:
        def result = source.poll()

        then:
        result.isPresent()
        result.get().payload == "Hello World"
        result.get().headers["http_status_code"] == 200
        result.get().headers["Content-Type"] == "text/plain"
    }

    def "should return empty Optional when HTTP request fails"() {
        given:
        def url = "http://example.com"
        def mockResponse = Mock(HttpResponse)
        
        mockResponse.statusCode() >> 404
        
        def source = new HttpMessageSource(url, null) {
            @Override
            protected HttpResponse<String> sendRequest(HttpRequest request) {
                return mockResponse
            }
        }

        when:
        def result = source.poll()

        then:
        !result.isPresent()
    }

    def "should return empty Optional when exception occurs"() {
        given:
        def url = "http://example.com"
        
        def source = new HttpMessageSource(url, null) {
            @Override
            protected HttpResponse<String> sendRequest(HttpRequest request) {
                throw new IOException("Connection failed")
            }
        }

        when:
        def result = source.poll()

        then:
        !result.isPresent()
    }
}
