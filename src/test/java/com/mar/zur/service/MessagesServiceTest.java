package com.mar.zur.service;

import com.mar.zur.model.Message;
import com.mar.zur.model.MessageSolvingResponse;
import com.mar.zur.service.impl.MessagesServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
@ConfigurationProperties(prefix = "messages.service")
public class MessagesServiceTest {
    private static final String GAME_ID = "gameId";
    private static final String AD_ID = "adId";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MessagesServiceImpl messagesService;

    private String messagesListUrl;
    private String solveMessageUrl;

    @Test
    public void getAllMessages() {
        Message message = mock(Message.class);
        List<Message> messages = new ArrayList<>();
        messages.add(message);

        Mockito.when(restTemplate.getForObject(getMessagesListUrl(), Message[].class, GAME_ID))
                .thenReturn(new Message[]{message});

        List<Message> serviceResult = messagesService.getAllMessages(GAME_ID);
        Assert.assertEquals(messages.get(0), serviceResult.get(0));
    }

    @Test
    public void postSolveMessage() {
        MessageSolvingResponse solvingResponse = mock(MessageSolvingResponse.class);

        Mockito.when(restTemplate.postForObject(eq(getSolveMessageUrl()), any(), eq(MessageSolvingResponse.class), anyMap()))
                .thenReturn(solvingResponse);

        Optional<MessageSolvingResponse> optServiceResult = messagesService.postSolveMessage(AD_ID, GAME_ID);
        if (optServiceResult.isPresent()) {
            MessageSolvingResponse serviceResult = optServiceResult.get();
            Assert.assertEquals(solvingResponse, serviceResult);
        } else {
            throw new NullPointerException("Message service returned null!");
        }
    }

    public String getMessagesListUrl() {
        return messagesListUrl;
    }

    public String getSolveMessageUrl() {
        return solveMessageUrl;
    }

    public void setMessagesListUrl(String messagesListUrl) {
        this.messagesListUrl = messagesListUrl;
    }

    public void setSolveMessageUrl(String solveMessageUrl) {
        this.solveMessageUrl = solveMessageUrl;
    }
}