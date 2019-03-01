package com.mar.zur.strategy.impl;

import com.mar.zur.model.Message;
import com.mar.zur.strategy.MessagePicker;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class LowestRewardPicker implements MessagePicker {

    @Override
    public Message pickOneToSolve(List<Message> messages) {
        return messages.stream()
                .min(Comparator.comparing(Message::getReward))
                .orElseThrow(NoSuchElementException::new);
    }
}
