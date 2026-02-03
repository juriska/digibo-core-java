package com.digibo.core.service;

import java.util.Map;

/**
 * NotifyService - Service interface for BONotify Oracle package
 * Handles notification operations for various systems
 */
public interface NotifyService {

    /**
     * Notify rates board
     * @return Result map with success status and message
     */
    Map<String, Object> notifyRatesBoard();

    /**
     * Notify FFO system
     * @return Result map with success status and message
     */
    Map<String, Object> notifyFfo();

    /**
     * Notify investment system
     * @return Result map with success status and message
     */
    Map<String, Object> notifyInvestment();

    /**
     * Notify mortgage loans system
     * @return Result map with success status and message
     */
    Map<String, Object> notifyMortgageLoans();

    /**
     * Update BO permissions
     * @return Result map with success status and message
     */
    Map<String, Object> updateBoPermissions();
}
