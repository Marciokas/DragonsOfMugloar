package com.mar.zur.service;

import com.mar.zur.model.Message;
import com.mar.zur.model.MessageSolvingResponse;
import com.mar.zur.model.URLConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MessagesService {
    private static final Logger logger = LoggerFactory.getLogger(MessagesService.class);

    @Autowired
    private RestTemplate restTemplate;

    public List<Message> getAllMessages(String gameId) {
        try {
            return Arrays.asList(restTemplate.getForObject(URLConstants.GET_ALL_MESSAGES_URL, Message[].class, gameId));
        } catch (HttpClientErrorException e) {
            logger.error("Message service was unable to receive message list. Error occured:{}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<MessageSolvingResponse> postSolveMessage(String adId, String gameId) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("gameId", gameId);
            params.put("adId", adId);
            MessageSolvingResponse response = restTemplate.postForObject(URLConstants.SOLVE_MESSAGE_URL, null, MessageSolvingResponse.class, params);
            return Optional.of(response);
        } catch (HttpClientErrorException e) {
            logger.error("Message service was unable to receive message solving result. Error occured:{}", e.getMessage());
            return Optional.empty();
        }
    }
}
