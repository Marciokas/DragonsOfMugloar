package com.mar.zur.service;

import com.mar.zur.model.InvestigationResponse;
import com.mar.zur.model.URLConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class InvestigationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private InvestigationService investigationService;

    @Test
    public void investigateReputation() {
        InvestigationResponse investigationResponse = mock(InvestigationResponse.class);

        Mockito.when(restTemplate.postForObject(URLConstants.INVESTIGATION_URL, null, InvestigationResponse.class, "test"))
                .thenReturn(investigationResponse);

        Optional<InvestigationResponse> optServiceResult = investigationService.investigateReputation("test");
        if (optServiceResult.isPresent()) {
            InvestigationResponse serviceResult = optServiceResult.get();
            Assert.assertEquals(investigationResponse, serviceResult);
        } else {
            throw new NullPointerException("Investigation service returned null!");
        }
    }
}