package com.mar.zur.service;

import com.mar.zur.model.ShopItem;
import com.mar.zur.model.ShopItemResponse;
import com.mar.zur.model.URLConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ShopServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ShopService shopService;

    @Test
    public void getAllItems() {
        ShopItem shopItem = mock(ShopItem.class);
        List<ShopItem> shopItems = new ArrayList<>();
        shopItems.add(shopItem);

        Mockito.when(restTemplate.getForObject(URLConstants.SHOP_ITEMS_LIST_URL, ShopItem[].class, "gameId"))
                .thenReturn(new ShopItem[]{shopItem});
        List<ShopItem> serviceResult = shopService.getAllItems("gameId");

        Assert.assertEquals(shopItems.get(0), serviceResult.get(0));
    }

    @Test
    public void buyItem() {
        ShopItemResponse shopItemResponse = mock(ShopItemResponse.class);

        Mockito.when(restTemplate.postForObject(eq(URLConstants.BUY_ITEM_URL), any(), eq(ShopItemResponse.class), anyMap()))
                .thenReturn(shopItemResponse);

        ShopItemResponse serviceResult = shopService.buyItem("itemId", "gameId").get();
        Assert.assertEquals(shopItemResponse, serviceResult);
    }

}