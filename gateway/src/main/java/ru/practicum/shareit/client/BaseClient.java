package ru.practicum.shareit.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestClient restClient;
    private final String baseUrl;

    public BaseClient(RestClient restClient, String baseUrl) {
        this.restClient = restClient;
        this.baseUrl = baseUrl;
    }

    protected <R> ResponseEntity<R> get(String path,
                                        @Nullable Long userId,
                                        @Nullable Map<String, Object> parameters,
                                        Class<R> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path(path);

        if (parameters != null) {
            parameters.forEach(builder::queryParam);
        }

        URI uri = builder.build().toUri();

        RestClient.RequestHeadersSpec<?> req = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON);

        if (userId != null) {
            req.header("X-Sharer-User-Id", userId.toString());
        }

        return req.retrieve().toEntity(responseType);
    }

    protected <R> ResponseEntity<R> get(String path, Class<R> responseType) {
        return get(path, null, null, responseType);
    }

    protected <R> ResponseEntity<R> get(String path, long userId, Class<R> responseType) {
        return get(path, userId, null, responseType);
    }


    protected <R> ResponseEntity<List<R>> getList(String path,
                                                  @Nullable Long userId,
                                                  @Nullable Map<String, Object> parameters,
                                                  Class<R[]> arrayType) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path(path);

        if (parameters != null) {
            parameters.forEach(builder::queryParam);
        }

        URI uri = builder.build().toUri();

        RestClient.RequestHeadersSpec<?> req = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON);

        if (userId != null) {
            req.header("X-Sharer-User-Id", userId.toString());
        }

        return req.retrieve().toEntity(new ParameterizedTypeReference<>() {
        });

    }

    protected <R> ResponseEntity<List<R>> getList(String path, Class<R[]> arrayType) {
        return getList(path, null, null, arrayType);
    }


    protected <R> ResponseEntity<List<R>> getList(String path, long userId, Class<R[]> arrayType) {
        return getList(path, userId, null, arrayType);
    }

    protected <R> ResponseEntity<List<R>> getList(String path, Map<String, Object> parameters,
                                                  Class<R[]> arrayType) {
        return getList(path, null, parameters, arrayType);
    }

    private <B, R> ResponseEntity<R> makeRequest(HttpMethod method,
                                                 String path,
                                                 @Nullable Long userId,
                                                 @Nullable Map<String, Object> parameters,
                                                 @Nullable B body,
                                                 Class<R> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path(path);

        if (parameters != null) {
            parameters.forEach(builder::queryParam);
        }

        URI uri = builder.build().toUri();

        RestClient.RequestBodySpec req = restClient.method(method)
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        if (userId != null) {
            req.header("X-Sharer-User-Id", userId.toString());
        }
        if (body != null) {
            req.body(body);
        }

        return req.retrieve().toEntity(responseType);
    }

    protected <R> ResponseEntity<R> post(String path, Object body, Class<R> responseType) {
        return makeRequest(HttpMethod.POST, path, null, null, body, responseType);
    }

    protected <R> ResponseEntity<R> post(String path, long userId, Object body, Class<R> responseType) {
        return makeRequest(HttpMethod.POST, path, userId, null, body, responseType);
    }

    protected <R> ResponseEntity<R> patch(String path, long userId, Object body, Class<R> responseType) {
        return makeRequest(HttpMethod.PATCH, path, userId, null, body, responseType);
    }

    protected <R> ResponseEntity<R> patch(String path, long userId, Map<String, Object> parameters,
                                          Class<R> responseType) {
        return makeRequest(HttpMethod.PATCH, path, userId, parameters, null, responseType);
    }

    protected <R> ResponseEntity<R> put(String path, long userId, Object body, Class<R> responseType) {
        return makeRequest(HttpMethod.PUT, path, userId, null, body, responseType);
    }

    protected <R> ResponseEntity<R> delete(String path, long userId, Class<R> responseType) {
        return makeRequest(HttpMethod.DELETE, path, userId, null, null, responseType);
    }
}