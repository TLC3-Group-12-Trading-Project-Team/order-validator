package com.example.ordervalidation.marketdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketDataService {

    private final MarketDataRepository marketDataRepository;

    @Autowired
    MarketDataService(MarketDataRepository mdr) {
        marketDataRepository = mdr;
    }

    public void clearAllData() {
        marketDataRepository.deleteAll();
    }

    public List<MarketData> appendData(List<MarketData> data) {
        return marketDataRepository.saveAll(data);
    }

    public MarketData getMarketDataByTicker(String product){
        return marketDataRepository.findByTicker(product).orElse(null);
    }
}