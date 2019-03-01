package com.mar.zur.processor;

import com.mar.zur.model.*;
import com.mar.zur.service.GameStarterService;
import com.mar.zur.service.InvestigationService;
import com.mar.zur.service.MessagesService;
import com.mar.zur.service.ShopService;
import com.mar.zur.strategy.MessagePicker;
import com.mar.zur.strategy.impl.EqualProbabilityPicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GameProcessor {
    private static final Logger logger = LoggerFactory.getLogger(GameProcessor.class);
    private static final String HPOT_ID = "hpot";
    private static final int HPOT_PRICE = 50;
    private static final int GAMES_COUNT = 15;
    private static final int NEED_BETTER_ITEMS_SCORE_RANGE = 1500;
    private static final int NEED_ITEMS_SCORE_RANGE = 1000;
    private static final int BETTER_ITEMS_COST = 300;
    private static final int SIMPLE_ITEMS_COST = 100;

    @Autowired
    private GameStarterService gameStarterService;
    @Autowired
    private MessagesService messagesService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private InvestigationService investigationService;

    private GameState gameState;
    private List<Message> currentMessages;
    private List<ShopItem> shopItems;

    @PostConstruct
    public void init() {
        playGameSeveralTimes(GAMES_COUNT);
    }

    private void playGameSeveralTimes(int times) {
        for (int i = 0; i < times; i++) {
            startNewGame();
            printGameResults();
        }
    }

    public void startNewGame() {
        initGameState();
        initMessageList();
        initShopItemsList();
        recursiveSolving(new EqualProbabilityPicker());
    }

    private void initMessageList() {
        currentMessages = new ArrayList<>(getNewMessages());
        if (currentMessages.isEmpty()) {
            throw new IllegalStateException("Game can not be started if there is no messages to be solved");
        }
    }

    private void initShopItemsList() {
        shopItems = new ArrayList<>(getShopItems());
        if (shopItems.isEmpty()) {
            throw new IllegalStateException("Game can not be started if there is no working shop that sells healing potions");
        }
    }

    private void initGameState() {
        Optional<GameState> optGameState = gameStarterService.getNewGameState();
        if (!optGameState.isPresent()) {
            throw new IllegalStateException("Game can not be started without initial game id");
        }
        gameState = optGameState.get();
    }

    private void recursiveSolving(MessagePicker picker) {
        if (gameState.getLives() != 0) {
            Optional<MessageSolvingResponse> optSolvingResponse = pickAndSolveMessage(picker);
            if (!optSolvingResponse.isPresent()) {
                reloadNewMessages();
            } else {
                MessageSolvingResponse solvingResponse = optSolvingResponse.get();
                executePostTurnActions(solvingResponse);
                enterTheShop();
            }
            recursiveSolving(picker);
        }
    }

    private void enterTheShop() {
        if (isOneLifeLeft()) {
            buyItem(HPOT_ID, HPOT_PRICE);
        } else {
            if (isRequiredOfBetterItems()) {
                buyItemToLevelUp(BETTER_ITEMS_COST);
            }
            if (isRequiredOfItems()) {
                buyItemToLevelUp(SIMPLE_ITEMS_COST);
            }
        }
    }

    private void buyItemToLevelUp(int itemCost) {
        Optional<ShopItem> optShopItem = getRandomItemOfGivenCost(itemCost);
        if (optShopItem.isPresent()) {
            ShopItem shopItem = optShopItem.get();
            buyItem(shopItem.getId(), shopItem.getCost());
        }
    }

    private void reloadNewMessages() {
        if (!gameState.isGameOver()) {
            currentMessages.clear();
            currentMessages.addAll(getNewMessages());
        }
    }

    private Optional<MessageSolvingResponse> pickAndSolveMessage(MessagePicker messagePicker) {
        Message messageToSolve = messagePicker.pickOneToSolve(currentMessages);
        currentMessages.remove(messageToSolve);
        return messagesService.postSolveMessage(messageToSolve.getDecryptedAdId(), gameState.getGameId());
    }

    private void buyItem(String itemId, int itemCost) {
        if (!gameState.isGameOver() && isEnoughGold(itemCost)) {
            Optional<ShopItemResponse> optBuyItemResponse = shopService.buyItem(itemId, gameState.getGameId());
            if (optBuyItemResponse.isPresent()) {
                ShopItemResponse shopItemResponse = optBuyItemResponse.get();
                executePostTurnActions(shopItemResponse);
            }
        }
    }

    private void executePostTurnActions(ShopItemResponse response) {
        gameState.setLevel(response.getLevel());
        executePostTurnActions((Response) response);
    }

    private void executePostTurnActions(MessageSolvingResponse response) {
        gameState.setScore(response.getScore());
        executePostTurnActions((Response) response);
    }

    private void executePostTurnActions(Response response) {
        updateGameState(response);
        decrementMessagesExpirationIndex();
        removeExpiredMessages();
        updateMessageListIfEmpty();
    }

    private void updateGameState(Response response) {
        gameState.setGold(response.getGold());
        gameState.setLives(response.getLives());
        gameState.setTurn(response.getTurn());
    }

    private Optional<ShopItem> getRandomItemOfGivenCost(int cost) {
        return shopItems.stream().filter(item -> item.getCost() == cost).findFirst();
    }

    private boolean isRequiredOfBetterItems() {
        return gameState.getScore() > NEED_BETTER_ITEMS_SCORE_RANGE;
    }

    private boolean isRequiredOfItems() {
        return gameState.getScore() > NEED_ITEMS_SCORE_RANGE;
    }

    private boolean isEnoughGold(int itemCost) {
        return gameState.getGold() > itemCost;
    }

    private boolean isOneLifeLeft() {
        return gameState.getLives() == 1;
    }

    private void removeExpiredMessages() {
        currentMessages = currentMessages.stream().filter(Message::isMessageValid).collect(Collectors.toList());
    }

    private void updateMessageListIfEmpty() {
        if (currentMessages.isEmpty() && !gameState.isGameOver()) {
            currentMessages.addAll(getNewMessages());
        }
    }

    private void decrementMessagesExpirationIndex() {
        currentMessages.stream().forEach(Message::decrementExpiration);
    }

    private List<Message> getNewMessages() {
        return messagesService.getAllMessages(gameState.getGameId());
    }

    private List<ShopItem> getShopItems() {
        return shopService.getAllItems(gameState.getGameId());
    }

    private void printGameResults() {
        logger.info("--------------------------------------------------------------------");
        logger.info("GAME OVER! You can see game statistics below.");
        logger.info(gameState.toString());
        logger.info("--------------------------------------------------------------------");
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public List<Message> getCurrentMessages() {
        return currentMessages;
    }

    public void setCurrentMessages(List<Message> currentMessages) {
        this.currentMessages = currentMessages;
    }

    public void setShopItems(List<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }
}
