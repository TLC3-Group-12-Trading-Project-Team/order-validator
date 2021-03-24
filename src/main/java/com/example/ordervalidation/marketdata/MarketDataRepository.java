package com.example.ordervalidation.marketdata;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
    Optional<MarketData> findByTicker(String ticker);
}