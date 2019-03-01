package com.mar.zur.service;

import com.mar.zur.model.ShopItem;
import com.mar.zur.model.ShopItemResponse;

import java.util.List;
import java.util.Optional;

public interface ShopService {
    List<ShopItem> getAllItems(String gameId);

    Optional<ShopItemResponse> buyItem(String itemId, String gameId);
}
