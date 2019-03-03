package com.mar.zur.service;

import com.mar.zur.model.InvestigationResponse;
import com.mar.zur.service.impl.InvestigationServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
@ConfigurationProperties(prefix = "investigation.service")
public class InvestigationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private InvestigationServiceImpl investigationService;

    private String investigateUrl;

    @Test
    public void investigateReputation() {
        InvestigationResponse investigationResponse = mock(InvestigationResponse.class);

        Mockito.when(restTemplate.postForObject(getInvestigateUrl(), null, InvestigationResponse.class, "test"))
                .thenReturn(investigationResponse);

        Optional<InvestigationResponse> optServiceResult = investigationService.investigateReputation("test");
        if (optServiceResult.isPresent()) {
            InvestigationResponse serviceResult = optServiceResult.get();
            Assert.assertEquals(investigationResponse, serviceResult);
        } else {
            throw new NullPointerException("Investigation service returned null!");
        }
    }

    public String getInvestigateUrl() {
        return investigateUrl;
    }

    public void setInvestigateUrl(String investigateUrl) {
        this.investigateUrl = investigateUrl;
    }
}