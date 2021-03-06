package com.mar.zur.service;

import com.mar.zur.model.ShopItem;
import com.mar.zur.model.ShopItemResponse;
import com.mar.zur.service.impl.ShopServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
@ConfigurationProperties(prefix = "shop.service")
public class ShopServiceTest {
    private static final String GAME_ID = "gameId";
    private static final String ITEM_ID = "itemId";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ShopServiceImpl shopService;

    private String itemsListUrl;
    private String buyItemUrl;

    @Test
    public void getAllItems() {
        ShopItem shopItem = mock(ShopItem.class);
        List<ShopItem> shopItems = new ArrayList<>();
        shopItems.add(shopItem);

        Mockito.when(restTemplate.getForObject(getItemsListUrl(), ShopItem[].class, GAME_ID))
                .thenReturn(new ShopItem[]{shopItem});

        List<ShopItem> serviceResult = shopService.getAllItems(GAME_ID);
        Assert.assertEquals(shopItems.get(0), serviceResult.get(0));
    }

    @Test
    public void buyItem() {
        ShopItemResponse shopItemResponse = mock(ShopItemResponse.class);

        Mockito.when(restTemplate.postForObject(eq(getBuyItemUrl()), any(), eq(ShopItemResponse.class), anyMap()))
                .thenReturn(shopItemResponse);

        Optional<ShopItemResponse> optServiceResult = shopService.buyItem(ITEM_ID, GAME_ID);
        if (optServiceResult.isPresent()) {
            ShopItemResponse serviceResult = optServiceResult.get();
            Assert.assertEquals(shopItemResponse, serviceResult);
        } else {
            throw new NullPointerException("Shop service returned null!");
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