package com.example.ordervalidation.marketdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table
@Entity(name = "MarketData")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketData {

    @Id
    @SequenceGenerator(
            name = "market_sequence",
            sequenceName = "market_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_sequence"
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private Long id;

    @JsonProperty(value = "LAST_TRADED_PRICE")
    @Column(nullable = false)
    private double lastTradedPrice;

    @JsonProperty(value = "BID_PRICE")
    @Column(nullable = false)
    private double bidPrice;

    @JsonProperty(value = "SELL_LIMIT")
    @Column(nullable = false)
    private int sellLimit;

    @JsonProperty(value = "MAX_PRICE_SHIFT")
    @Column(nullable = false)
    private double maxPriceShift;

    @JsonProperty(value = "TICKER")
    @Column(nullable = false)
    private String ticker;

    @JsonProperty(value = "ASK_PRICE")
    @Column(nullable = false)
    private double askPrice;

    @JsonProperty(value = "BUY_LIMIT")
    @Column(nullable = false)
    private int buyLimit;
}

