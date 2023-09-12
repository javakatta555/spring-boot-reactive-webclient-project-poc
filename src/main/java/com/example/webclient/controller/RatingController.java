package com.example.webclient.controller;

import com.example.webclient.model.Rating;
import com.example.webclient.service.WebClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class RatingController {

    @Autowired
    private WebClientService webClientService;

    @GetMapping(value = "/rating")
    public Mono<Rating> getRating(){
        log.info("inside rating");
        return webClientService.getRating("http://localhost:8081/rating");
    }
}
