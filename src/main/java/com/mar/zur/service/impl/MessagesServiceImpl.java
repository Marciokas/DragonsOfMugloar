package com.mar.zur.service.impl;

import com.mar.zur.model.Message;
import com.mar.zur.model.MessageSolvingResponse;
import com.mar.zur.service.MessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@ConfigurationProperties(prefix = "messages.service")
public class MessagesServiceImpl implements MessagesService {
    private static final Logger logger = LoggerFactory.getLogger(MessagesServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    private String messagesListUrl;
    private String solveMessageUrl;

    @Override
    public List<Message> getAllMessages(String gameId) {
        try {
            return Arrays.asList(restTemplate.getForObject(getMessagesListUrl(), Message[].class, gameId));
        } catch (HttpClientErrorException e) {
            logger.error("Message service was unable to receive message list. Error occured:{}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<MessageSolvingResponse> postSolveMessage(String adId, String gameId) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("gameId", gameId);
            params.put("adId", adId);
            MessageSolvingResponse response = restTemplate.postForObject(getSolveMessageUrl(), null, MessageSolvingResponse.class, params);
            return Optional.of(response);
        } catch (HttpClientErrorException e) {
            logger.error("Message service was unable to receive message solving result. Error occured:{}", e.getMessage());
            return Optional.empty();
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
