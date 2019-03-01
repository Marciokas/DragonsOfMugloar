package com.mar.zur.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopItemResponse extends Response {

    private boolean shoppingSuccess;
    private int level;

    public boolean isShoppingSuccess() {
        return shoppingSuccess;
    }

    public void setShoppingSuccess(boolean shoppingSuccess) {
        this.shoppingSuccess = shoppingSuccess;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "ShopItemResponse{" +
                "shoppingSuccess=" + shoppingSuccess +
                ", level=" + level +
                '}';
    }
}
