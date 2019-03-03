package com.mar.zur.processor;

import com.mar.zur.model.GameState;
import com.mar.zur.model.ShopItem;
import com.mar.zur.model.ShopItemResponse;
import com.mar.zur.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ShopProcessor {
    @Value("${shop.items.cost.simpleItems}")
    private int simpleItemsCost;
    @Value("${shop.items.cost.betterItems}")
    private int betterItemsCost;
    @Value("${shop.items.id.hpot}")
    private String hpotId;
    @Value("${shop.items.cost.hpot}")
    private int hpotCost;

    @Autowired
    private ShopService shopService;

    public List<ShopItem> getShopItems(String gameId) {
        return shopService.getAllItems(gameId);
    }

    public void enterTheShop(GameState gameState) {
        if (gameState.isOneLifeLeft()) {
            buyItem(gameState, getHpotId(), getHpotCost());
        } else {
            if (gameState.isRequiredOfBetterItems()) {
                buyItemToLevelUp(gameState, getBetterItemsCost());
            }
            if (gameState.isRequiredOfItems()) {
                buyItemToLevelUp(gameState, getSimpleItemsCost());
            }
        }
    }

    public void buyItemToLevelUp(GameState gameState, int itemCost) {
        Optional<ShopItem> optShopItem = gameState.getRandomItemOfGivenCost(itemCost);
        if (optShopItem.isPresent()) {
            ShopItem shopItem = optShopItem.get();
            buyItem(gameState, shopItem.getId(), shopItem.getCost());
        }
    }

    public void buyItem(GameState gameState, String itemId, int itemCost) {
        if (!gameState.isGameOver() && gameState.isEnoughGold(itemCost)) {
            Optional<ShopItemResponse> optBuyItemResponse = shopService.buyItem(itemId, gameState.getGame().getGameId());
            if (optBuyItemResponse.isPresent()) {
                ShopItemResponse shopItemResponse = optBuyItemResponse.get();
                executePostTurnActions(gameState, shopItemResponse);
            }
        }
    }

    private void executePostTurnActions(GameState gameState, ShopItemResponse response) {
        gameState.getGame().setLevel(response.getLevel());
        gameState.executePostTurnActions(response);
    }

    public int getSimpleItemsCost() {
        return simpleItemsCost;
    }

    public void setSimpleItemsCost(int simpleItemsCost) {
        this.simpleItemsCost = simpleItemsCost;
    }

    public int getBetterItemsCost() {
        return betterItemsCost;
    }

    public void setBetterItemsCost(int betterItemsCost) {
        this.betterItemsCost = betterItemsCost;
    }

    public String getHpotId() {
        return hpotId;
    }

    public void setHpotId(String hpotId) {
        this.hpotId = hpotId;
    }

    public int getHpotCost() {
        return hpotCost;
    }

    public void setHpotCost(int hpotCost) {
        this.hpotCost = hpotCost;
    }
}
