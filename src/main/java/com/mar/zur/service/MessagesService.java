package com.mar.zur.service;

import com.mar.zur.model.Message;
import com.mar.zur.model.MessageSolvingResponse;

import java.util.List;
import java.util.Optional;

public interface MessagesService {
    List<Message> getAllMessages(String gameId);

    Optional<MessageSolvingResponse> postSolveMessage(String adId, String gameId);
}
