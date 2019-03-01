package com.mar.zur.service.impl;

import com.mar.zur.model.ShopItem;
import com.mar.zur.model.ShopItemResponse;
import com.mar.zur.model.URLConstants;
import com.mar.zur.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ShopServiceImpl implements ShopService {
    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<ShopItem> getAllItems(String gameId) {
        try {
            return Arrays.asList(restTemplate.getForObject(URLConstants.SHOP_ITEMS_LIST_URL, ShopItem[].class, gameId));
        } catch (HttpClientErrorException e) {
            logger.error("Shop service was unable to receive items list. Error received:{}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<ShopItemResponse> buyItem(String itemId, String gameId) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("gameId", gameId);
            params.put("itemId", itemId);
            ShopItemResponse response = restTemplate.postForObject(URLConstants.BUY_ITEM_URL, null, ShopItemResponse.class, params);
            return Optional.of(response);
        } catch (HttpClientErrorException e) {
            logger.error("Shop service was unable to receive a response when buying an item. Error occured:{}", e.getMessage());
            return Optional.empty();
        }
    }
}
