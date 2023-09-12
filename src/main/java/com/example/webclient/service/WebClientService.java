package com.example.webclient.service;

import com.example.webclient.exception.BackendException;
import com.example.webclient.exception.ErrorResponse;
import com.example.webclient.model.Rating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
public class WebClientService {

    @Autowired
    private WebClient webClient;

    public Mono<Rating> getRating(String url){
        return webClient.get().uri(URI.create(url))
                .retrieve()
                .onStatus(HttpStatusCode::isError, error->mapError(error,error.statusCode().toString()))
                //.block()  //we can use block to work web client as synchronous

                .bodyToMono(Rating.class)
                .switchIfEmpty(Mono.defer(()->
                        Mono.error(new RuntimeException("Empty response"))))
                .doOnError(error->log.info("Error is  {}",error.getMessage()));

    }

    public <T, S> Mono<S> postMono(String url, T body, Class<S> clasz, HttpHeaders httpHeaders){
        log.info("Request Body - {}",body);
        return webClient.post()
                .uri(URI.create(url))
                .bodyValue(body)
                .headers(h->h.addAll(httpHeaders))
                .retrieve()
                .bodyToMono(clasz)
                .doOnError(err->{
                    if(err instanceof WebClientResponseException)
                        log.error("Exception in post mono for url  - {} Body  - {} Headers - {} Exception - {} Response - {}",url,body,httpHeaders,err,((WebClientResponseException)err).getResponseBodyAsString());
                    else
                        log.error("Exception in post mono for url  - {} Body  - {}  Headers - {} Exception",url,body,httpHeaders,err);
                }).doOnSuccess(resp->{
                    log.info("Received response - {}",resp);
                });
    }

    private Mono<BackendException> mapError(ClientResponse resp, String errorCode)
            throws BackendException {
        return resp.bodyToMono(String.class).flatMap(s -> {
            log.error("[rating service error] Error received from rating service webclient {}", s);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorMessage(s);
            HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            resp.statusCode();
            if (HttpStatus.resolve(resp.statusCode().value()) != null) {
                errorResponse.setStatus(resp.statusCode().value());
                httpStatus = HttpStatus.resolve(resp.statusCode().value());
            } else {
                errorResponse.setStatus(resp.statusCode().value());
            }
            throw new BackendException(httpStatus, errorCode, Boolean.TRUE, errorResponse);
        });
    }

}
