package com.mar.zur.strategy;

import com.mar.zur.model.Message;

import java.util.List;

public interface MessagePicker {

    Message pickOneToSolve(List<Message> messages);
}
