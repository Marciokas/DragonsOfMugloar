package com.mar.zur.service;

import com.mar.zur.model.GameState;
import com.mar.zur.model.URLConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GameStarterService {
    private static final Logger logger = LoggerFactory.getLogger(GameStarterService.class);

    @Autowired
    private RestTemplate restTemplate;

    public Optional<GameState> getNewGameState() {
        try {
            return Optional.of(restTemplate.postForObject(URLConstants.GAME_STARTER_URL, null, GameState.class));
        } catch (HttpClientErrorException e) {
            logger.error("Game starter service was unable to receive a response. Error received:{}", e.getMessage());
            return Optional.empty();
        }
    }


}
