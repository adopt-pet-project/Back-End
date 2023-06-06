package com.adoptpet.server.docs;


import org.springframework.http.HttpHeaders;

public abstract class GenerateMockToken {

    public static HttpHeaders getToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYXZhanVuZ3N1a0BnbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjg2MDU4NDAzLCJleHAiOjE2ODcyNjgwMDN9.8PGKAPB8n4qZDI3tK-S9OUAFgtKVu3VIpldoQ1ZK824";
        httpHeaders.add(HttpHeaders.AUTHORIZATION, token);
        return httpHeaders;
    }
}
