package com.mar.zur.service;

import com.mar.zur.model.GameState;
import com.mar.zur.model.URLConstants;
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
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GameStarterServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GameStarterServiceImpl gameStarterService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getNewGameState() {
        GameState gameState = mock(GameState.class);

        Mockito.when(restTemplate.postForObject(URLConstants.GAME_STARTER_URL, null, GameState.class))
                .thenReturn(gameState);

        Optional<GameState> optServiceResult = gameStarterService.getNewGameState();
        if (optServiceResult.isPresent()) {
            GameState serviceResult = optServiceResult.get();
            Assert.assertEquals(gameState, serviceResult);
        } else {
            throw new NullPointerException("Game starter service returned null!");
        }
    }

}