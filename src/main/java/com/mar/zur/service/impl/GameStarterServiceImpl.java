package com.mar.zur.service.impl;

import com.mar.zur.model.GameState;
import com.mar.zur.service.GameStarterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@ConfigurationProperties(prefix = "game.starter")
public class GameStarterServiceImpl implements GameStarterService {
    private static final Logger logger = LoggerFactory.getLogger(GameStarterServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    private String newGameUrl;

    @Override
    public Optional<GameState> getNewGameState() {
        try {
            return Optional.of(restTemplate.postForObject(getNewGameUrl(), null, GameState.class));
        } catch (HttpClientErrorException e) {
            logger.error("Game starter service was unable to receive a response. Error received:{}", e.getMessage());
            return Optional.empty();
        }
    }

    public String getNewGameUrl() {
        return newGameUrl;
    }

    public void setNewGameUrl(String newGameUrl) {
        this.newGameUrl = newGameUrl;
    }
}
