package com.mar.zur.service.impl;

import com.mar.zur.model.InvestigationResponse;
import com.mar.zur.model.URLConstants;
import com.mar.zur.service.InvestigationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class InvestigationServiceImpl implements InvestigationService {
    private static final Logger logger = LoggerFactory.getLogger(InvestigationServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<InvestigationResponse> investigateReputation(String gameId) {
        try {
            return Optional.of(restTemplate.postForObject(URLConstants.INVESTIGATION_URL, null, InvestigationResponse.class, gameId));
        } catch (HttpClientErrorException e) {
            logger.error("Investigation service was unable to receive a response. Error received:{}", e.getMessage());
            return Optional.empty();
        }
    }
}
