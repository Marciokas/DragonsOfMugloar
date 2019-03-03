package com.mar.zur.service.impl;

import com.mar.zur.model.ShopItem;
import com.mar.zur.model.ShopItemResponse;
import com.mar.zur.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@ConfigurationProperties(prefix = "shop.service")
public class ShopServiceImpl implements ShopService {
    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    private String itemsListUrl;
    private String buyItemUrl;

    @Override
    public List<ShopItem> getAllItems(String gameId) {
        try {
            return Arrays.asList(restTemplate.getForObject(getItemsListUrl(), ShopItem[].class, gameId));
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
            ShopItemResponse response = restTemplate.postForObject(getBuyItemUrl(), null, ShopItemResponse.class, params);
            return Optional.of(response);
        } catch (HttpClientErrorException e) {
            logger.error("Shop service was unable to receive a response when buying an item. Error occured:{}", e.getMessage());
            return Optional.empty();
        }
    }

    public String getItemsListUrl() {
        return itemsListUrl;
    }

    public String getBuyItemUrl() {
        return buyItemUrl;
    }

    public void setItemsListUrl(String itemsListUrl) {
        this.itemsListUrl = itemsListUrl;
    }

    public void setBuyItemUrl(String buyItemUrl) {
        this.buyItemUrl = buyItemUrl;
    }
}
