package com.mar.zur.model;

public class URLConstants {
    private URLConstants() {}

    public static final String GAME_STARTER_URL = "https://www.dragonsofmugloar.com/api/v2/game/start";
    public static final String GET_ALL_MESSAGES_URL = "https://www.dragonsofmugloar.com/api/v2/{gameId}/messages";
    public static final String SOLVE_MESSAGE_URL = "https://www.dragonsofmugloar.com/api/v2/{gameId}/solve/{adId}";
    public static final String SHOP_ITEMS_LIST_URL = "https://www.dragonsofmugloar.com/api/v2/{gameId}/shop";
    public static final String BUY_ITEM_URL = "https://www.dragonsofmugloar.com/api/v2/{gameId}/shop/buy/{itemId}";
    public static final String INVESTIGATION_URL = "https://www.dragonsofmugloar.com/api/v2/{gameId}/investigate/reputation";
}
