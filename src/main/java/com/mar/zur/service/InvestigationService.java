package com.mar.zur.service;

import com.mar.zur.model.InvestigationResponse;

import java.util.Optional;

public interface InvestigationService {
    Optional<InvestigationResponse> investigateReputation(String gameId);
}
