package com.exchange;

import com.exchange.model.Money;
import com.exchange.model.Pair;
import com.exchange.model.RateUnavailableException;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

public class CalculatorTest {
    private static String USD = "USD";
    private static String JPY = "JPY";

    public ForexEngine forexEngine = Mockito.mock(ForexEngine.class);
    public Calculator calculator = new Calculator(forexEngine);

    @Test
    public void shouldExchangeWithDirectRate() throws Exception{
        Mockito.when(forexEngine.getExchangeRate(new Pair(USD, JPY))).thenReturn(BigDecimal.TEN);
        Money jpy = calculator.exchange(
                new Money(BigDecimal.valueOf(20), Currency.getInstance(USD)),
                Currency.getInstance(JPY)
        );

        assertThat(jpy.getAmount()).isCloseTo(BigDecimal.valueOf(200), withinPercentage(1));
        assertThat(jpy.getCurrency()).isEqualTo(Currency.getInstance(JPY));
    }

    @Test
    public void shouldExchangeWithInvertedRate() throws Exception{
        Mockito.when(forexEngine.getExchangeRate(new Pair(JPY, USD))).thenThrow(new RateUnavailableException());
        Mockito.when(forexEngine.getExchangeRate(new Pair(USD, JPY))).thenReturn(BigDecimal.TEN);

        Money usd = calculator.exchange(
                new Money(BigDecimal.valueOf(200), Currency.getInstance(JPY)),
                Currency.getInstance(USD));

        assertThat(usd.getAmount()).isCloseTo(BigDecimal.valueOf(20), withinPercentage(1));
        assertThat(usd.getCurrency()).isEqualTo(Currency.getInstance(USD));
    }
}
