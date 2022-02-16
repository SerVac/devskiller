package com.exchange;

import com.exchange.model.Bid;
import com.exchange.model.Pair;
import com.exchange.model.QuoteOutdatedException;
import com.exchange.model.RateUnavailableException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ForexEngine {
    private final Map<Pair, Bid> rates = new HashMap<>();

    /**
     *
     * @param pair
     * @param exchangeRate
     * @param quoteTime
     * @return exchange rate change: positive if the new rate is higher, negative if lower and zero if the previous value is not
     * @throws QuoteOutdatedException when passes quoteTime is older than the one already stored in the rates map
     */
    public BigDecimal update(Pair pair, BigDecimal exchangeRate, ZonedDateTime quoteTime) throws QuoteOutdatedException {
        Bid mapPair = null;
        if(rates.containsKey(pair)){
            mapPair = rates.get(pair);
            if(mapPair.getQuoteTime().isAfter(quoteTime)){
                throw  new QuoteOutdatedException("errr");
            }
        }

        Bid newBid = new Bid(exchangeRate, quoteTime);
        BigDecimal rate = exchangeRate;
        if(mapPair != null){
            rate = mapPair.getExchangeRate();
        }

        rates.put(pair, newBid);
        return  newBid.getExchangeRate().subtract(rate);
    }

    public BigDecimal getExchangeRate(Pair pair) throws RateUnavailableException{
        return Optional.ofNullable(rates.get(pair))
                .map(Bid::getExchangeRate)
                .orElseThrow(RateUnavailableException::new);
    }
}
