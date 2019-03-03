package com.mar.zur.service;

import com.mar.zur.model.Game;

import java.util.Optional;

public interface GameStarterService {
    Optional<Game> getNewGameState();
}
