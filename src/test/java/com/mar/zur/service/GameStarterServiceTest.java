package com.mar.zur.service;

import com.mar.zur.model.Game;
import com.mar.zur.service.impl.GameStarterServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
@ConfigurationProperties(prefix = "game.starter")
public class GameStarterServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GameStarterServiceImpl gameStarterService;

    private String newGameUrl;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getNewGameState() {
        Game game = mock(Game.class);

        Mockito.when(restTemplate.postForObject(getNewGameUrl(), null, Game.class))
                .thenReturn(game);

        Optional<Game> optServiceResult = gameStarterService.getNewGameState();
        if (optServiceResult.isPresent()) {
            Game serviceResult = optServiceResult.get();
            Assert.assertEquals(game, serviceResult);
        } else {
            throw new NullPointerException("Game starter service returned null!");
        }
    }

    public String getNewGameUrl() {
        return newGameUrl;
    }

    public void setNewGameUrl(String newGameUrl) {
        this.newGameUrl = newGameUrl;
    }
}