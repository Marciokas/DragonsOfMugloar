package com.mar.zur.strategy.impl;

import com.mar.zur.model.Message;
import com.mar.zur.strategy.MessagePicker;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;

public class EqualProbabilityPicker implements MessagePicker {

    private MessagePicker lowestRewardPicker = new LowestRewardPicker();
    private String probability;

    @Override
    public Message pickOneToSolve(List<Message> messages) {
        Message message;
        if (StringUtils.isEmpty(probability)) {
            message = getMessageWithLowestReward(messages);
        } else {
            try {
                message = getMessageWithTheSameProbability(messages);
            } catch (NoSuchElementException e) {
                message = getMessageWithLowestReward(messages);
            }
        }
        probability = message.getProbability();
        return message;
    }

    private Message getMessageWithLowestReward(List<Message> messages) {
        return lowestRewardPicker.pickOneToSolve(messages);
    }

    private Message getMessageWithTheSameProbability(List<Message> messages) {
        return messages.stream().filter(m -> probability.equals(m.getProbability()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }
}
