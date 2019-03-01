package com.mar.zur.service;

import com.mar.zur.model.Message;
import com.mar.zur.model.MessageSolvingResponse;
import com.mar.zur.model.URLConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MessagesServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MessagesService messagesService;

    @Test
    public void getAllMessages() {
        Message message = mock(Message.class);
        List<Message> messages = new ArrayList<>();
        messages.add(message);

        Mockito.when(restTemplate.getForObject(URLConstants.GET_ALL_MESSAGES_URL, Message[].class, "gameId"))
                .thenReturn(new Message[]{message});
        List<Message> serviceResult = messagesService.getAllMessages("gameId");

        Assert.assertEquals(messages.get(0), serviceResult.get(0));
    }

    @Test
    public void postSolveMessage() {
        MessageSolvingResponse solvingResponse = mock(MessageSolvingResponse.class);

        Mockito.when(restTemplate.postForObject(eq(URLConstants.SOLVE_MESSAGE_URL), any(), eq(MessageSolvingResponse.class), anyMap()))
                .thenReturn(solvingResponse);

        MessageSolvingResponse serviceResult = messagesService.postSolveMessage("adId", "gameId").get();
        Assert.assertEquals(solvingResponse, serviceResult);
    }

}