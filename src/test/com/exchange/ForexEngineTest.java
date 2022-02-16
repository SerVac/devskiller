package com.exchange;

import com.exchange.model.Pair;
import com.exchange.model.QuoteOutdatedException;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ForexEngineTest {

    private static String USD = "USD";
    private static String JPY = "JPY";

    private ForexEngine forexEngine = new ForexEngine();

    @Test
    public void shouldSetRate() throws Exception {
        BigDecimal update = forexEngine.update(new Pair(USD, JPY), BigDecimal.valueOf(109.82), ZonedDateTime.now());
//        assertThat(update).isEqualTo(BigDecimal.ZERO);
        assertTrue(update.compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void shouldUpdateRate() throws Exception {
        ZonedDateTime firstDate = ZonedDateTime.of(2017, 6, 2, 20, 21, 21, 0, ZoneId.systemDefault());
        ZonedDateTime newerDate = firstDate.plusMinutes(1);

        forexEngine.update(new Pair(USD, JPY), BigDecimal.valueOf(109.82), firstDate);
        BigDecimal update = forexEngine.update(new Pair(USD, JPY), BigDecimal.valueOf(109.83), newerDate);

        assertThat(update).isEqualTo(BigDecimal.valueOf(0.01));
//        assertTrue(update.compareTo(BigDecimal.valueOf(0.01)) == 0);
    }

    @Test
    public void shouldUpdateOutdatedRate() throws Exception{
        ForexEngine forexEngine = new ForexEngine();
        ZonedDateTime firstDate = ZonedDateTime.of(2017, 6, 2, 20, 21, 21, 0, ZoneId.systemDefault());
        ZonedDateTime earlierDate = firstDate.minusDays(1);

        forexEngine.update(new Pair(USD, JPY), BigDecimal.valueOf(109.82), firstDate);
        Throwable thrown = catchThrowable(() -> forexEngine.update(new Pair(USD, JPY), BigDecimal.valueOf(109.83), earlierDate));
        assertThat(thrown).isInstanceOf(QuoteOutdatedException.class);
    }


}
