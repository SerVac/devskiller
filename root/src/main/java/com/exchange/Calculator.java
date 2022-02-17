package com.exchange;

import com.exchange.model.Money;
import com.exchange.model.Pair;
import com.exchange.model.RateUnavailableException;

import java.math.BigDecimal;
import java.util.Currency;

public class Calculator {

    private final ForexEngine forexEngine;

    public Calculator(ForexEngine forexEngine) {
        this.forexEngine = forexEngine;
    }

    Money exchange(Money amount, Currency convertTo) throws RateUnavailableException {
        Pair pair = new Pair(amount.getCurrency().getCurrencyCode(), convertTo.getCurrencyCode());
        BigDecimal rate = forexEngine.getExchangeRate(pair);
        return new Money(amount.getAmount().multiply(rate), convertTo);
    }
}
