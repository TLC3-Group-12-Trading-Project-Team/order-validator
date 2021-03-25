package com.example.ordervalidation.marketdata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ExchangeData {

    @JsonProperty("LAST_TRADED_PRICE")
    private Double LAST_TRADED_PRICE;

    @JsonProperty("BID_PRICE")
    private Double BID_PRICE;

    @JsonProperty("SELL_LIMIT")
    private int SELL_LIMIT;

    @JsonProperty("MAX_PRICE_SHIFT")
    private Double MAX_PRICE_SHIFT;

    @JsonProperty("TICKER")
    private String TICKER;

    @JsonProperty("ASK_PRICE")
    private Double ASK_PRICE;

    @JsonProperty("BUY_LIMIT")
    private int BUY_LIMIT;
}
