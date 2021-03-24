package com.example.ordervalidation.marketdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webhooks")
public class MarketDataController {

    private static Logger logger = LoggerFactory.getLogger(MarketDataController.class);
    private final MarketDataService marketDataService;

    @Autowired
    MarketDataController(MarketDataService mds) {
        marketDataService = mds;
    }

    @PostMapping("/market-data")
    public void onMarketDataUpdate(@RequestBody List<MarketData> md) {
        logger.info(md.toString());

        // * clear the table data
        marketDataService.clearAllData();

        // * recreate the table with new data
        marketDataService.appendData(md);
    }
}