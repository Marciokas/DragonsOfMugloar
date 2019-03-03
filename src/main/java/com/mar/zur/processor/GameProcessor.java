package com.mar.zur.processor;

import com.mar.zur.model.*;
import com.mar.zur.service.GameStarterService;
import com.mar.zur.service.InvestigationService;
import com.mar.zur.service.MessagesService;
import com.mar.zur.strategy.MessagePicker;
import com.mar.zur.strategy.impl.EqualProbabilityPicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GameProcessor {

    @Value("${game.retry.count}")
    private int gameRetryCount;
    @Value("${shop.processor.enabled}")
    private boolean shopProcessorEnabled;

    @Autowired
    private GameStarterService gameStarterService;
    @Autowired
    private MessagesService messagesService;
    @Autowired
    private InvestigationService investigationService;
    @Autowired
    private ShopProcessor shopProcessor;

    @PostConstruct
    public void init() {
        playGameSeveralTimes(getGameRetryCount());
    }

    private void playGameSeveralTimes(int times) {
        for (int i = 0; i < times; i++) {
            startNewGame();
        }
    }

    public void startNewGame() {
        GameState gameState = builGameState();
        recursiveSolving(gameState, new EqualProbabilityPicker());
        gameState.logGameResults();
    }

    private GameState builGameState() {
        Game game = initGameState();
        List<Message> currentMessages = initMessageList(game.getGameId());
        List<ShopItem> shopItems = initShopItemsList(game.getGameId());
        return new GameState(game, currentMessages, shopItems);
    }

    private Game initGameState() {
        return gameStarterService.getNewGameState().orElseThrow(() -> new IllegalStateException("Game can not be started without initial game id"));
    }

    private List<Message> initMessageList(String gameId) {
        List<Message> currentMessages = new ArrayList<>(getNewMessages(gameId));
        if (currentMessages.isEmpty()) {
            throw new IllegalStateException("Game can not be started if there is no messages to be solved");
        }
        return currentMessages;
    }

    private List<ShopItem> initShopItemsList(String gameId) {
        List<ShopItem> shopItems = new ArrayList<>(shopProcessor.getShopItems(gameId));
        if (shopItems.isEmpty()) {
            throw new IllegalStateException("Game can not be started if there is no working shop that sells healing potions");
        }
        return shopItems;
    }

    private void recursiveSolving(GameState gameState, MessagePicker picker) {
        if (gameState.getGame().getLives() != 0) {
            updateMessageListIfEmpty(gameState);
            Optional<MessageSolvingResponse> optSolvingResponse = pickAndSolveMessage(gameState, picker);
            if (!optSolvingResponse.isPresent()) {
                reloadNewMessages(gameState);
            } else {
                MessageSolvingResponse solvingResponse = optSolvingResponse.get();
                executePostTurnActions(gameState, solvingResponse);
                if (shopProcessorEnabled) {
                    shopProcessor.enterTheShop(gameState);
                }
            }
            recursiveSolving(gameState, picker);
        }
    }

    private Optional<MessageSolvingResponse> pickAndSolveMessage(GameState gameState, MessagePicker messagePicker) {
        Message messageToSolve = messagePicker.pickOneToSolve(gameState.getCurrentMessages());
        gameState.getCurrentMessages().remove(messageToSolve);
        return messagesService.postSolveMessage(messageToSolve.getDecryptedAdId(), gameState.getGame().getGameId());
    }

    private void executePostTurnActions(GameState gameState, MessageSolvingResponse response) {
        gameState.getGame().setScore(response.getScore());
        gameState.executePostTurnActions(response);
    }

    private void updateMessageListIfEmpty(GameState gameState) {
        if (gameState.getCurrentMessages().isEmpty() && !gameState.isGameOver()) {
            gameState.getCurrentMessages().addAll(getNewMessages(gameState.getGame().getGameId()));
        }
    }

    private void reloadNewMessages(GameState gameState) {
        if (!gameState.isGameOver()) {
            gameState.getCurrentMessages().clear();
            gameState.getCurrentMessages().addAll(getNewMessages(gameState.getGame().getGameId()));
        }
    }

    private List<Message> getNewMessages(String gameId) {
        return messagesService.getAllMessages(gameId);
    }

    public int getGameRetryCount() {
        return gameRetryCount;
    }

    public void setGameRetryCount(int gameRetryCount) {
        this.gameRetryCount = gameRetryCount;
    }

    public boolean isShopProcessorEnabled() {
        return shopProcessorEnabled;
    }

    public void setShopProcessorEnabled(boolean shopProcessorEnabled) {
        this.shopProcessorEnabled = shopProcessorEnabled;
    }
}