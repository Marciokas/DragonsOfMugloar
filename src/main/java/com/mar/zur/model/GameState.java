package com.mar.zur.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameState {
    private static final Logger logger = LoggerFactory.getLogger(GameState.class);

    @Value("${score.range.betterItems}")
    private int betterItemsScoreRange;
    @Value("${score.range.simpleItems}")
    private int simpleItemsScoreRange;

    private Game game;
    private List<Message> currentMessages;
    private List<ShopItem> shopItems;

    public GameState(Game game, List<Message> currentMessages, List<ShopItem> shopItems) {
        this.game = game;
        this.currentMessages = currentMessages;
        this.shopItems = shopItems;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Message> getCurrentMessages() {
        return currentMessages;
    }

    public void setCurrentMessages(List<Message> currentMessages) {
        this.currentMessages = currentMessages;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(List<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public int getBetterItemsScoreRange() {
        return betterItemsScoreRange;
    }

    public void setBetterItemsScoreRange(int betterItemsScoreRange) {
        this.betterItemsScoreRange = betterItemsScoreRange;
    }

    public int getSimpleItemsScoreRange() {
        return simpleItemsScoreRange;
    }

    public void setSimpleItemsScoreRange(int simpleItemsScoreRange) {
        this.simpleItemsScoreRange = simpleItemsScoreRange;
    }

    public boolean isGameOver() {
        return getGame().getLives() == 0;
    }

    public void executePostTurnActions(Response response) {
        updateGameState(response);
        decrementMessagesExpirationIndex();
        removeExpiredMessages();
    }

    public Optional<ShopItem> getRandomItemOfGivenCost(int cost) {
        return shopItems.stream().filter(item -> item.getCost() == cost).findFirst();
    }

    public boolean isRequiredOfBetterItems() {
        return game.getScore() > getBetterItemsScoreRange();
    }

    public boolean isRequiredOfItems() {
        return game.getScore() > getSimpleItemsScoreRange();
    }

    public boolean isEnoughGold(int itemCost) {
        return game.getGold() > itemCost;
    }

    public boolean isOneLifeLeft() {
        return game.getLives() == 1;
    }

    public void logGameResults() {
        logger.info("--------------------------------------------------------------------");
        logger.info("GAME OVER! You can see game statistics below.");
        logger.info(getGame().toString());
        logger.info("--------------------------------------------------------------------");
    }

    private void updateGameState(Response response) {
        getGame().setGold(response.getGold());
        getGame().setLives(response.getLives());
        getGame().setTurn(response.getTurn());
    }

    private void removeExpiredMessages() {
        currentMessages = currentMessages.stream().filter(Message::isMessageValid).collect(Collectors.toList());
    }

    private void decrementMessagesExpirationIndex() {
        currentMessages.stream().forEach(Message::decrementExpiration);
    }

}
