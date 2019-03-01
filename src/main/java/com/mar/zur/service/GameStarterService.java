package com.mar.zur.service;

import com.mar.zur.model.GameState;

import java.util.Optional;

public interface GameStarterService {
    Optional<GameState> getNewGameState();
}
