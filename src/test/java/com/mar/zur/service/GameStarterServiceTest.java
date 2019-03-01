package com.mar.zur.service;

import com.mar.zur.model.GameState;
import com.mar.zur.model.URLConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GameStarterServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GameStarterService gameStarterService;

    @Test
    public void getNewGameState() {
        GameState gameState = mock(GameState.class);

        Mockito.when(restTemplate.postForObject(URLConstants.GAME_STARTER_URL, null, GameState.class))
                .thenReturn(gameState);
        GameState serviceResult = gameStarterService.getNewGameState().get();
        Assert.assertEquals(gameState, serviceResult);
    }

}