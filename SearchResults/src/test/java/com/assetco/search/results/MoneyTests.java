package com.assetco.search.results;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class MoneyTests {
  private static final String anyAmount = "123.40";
  private Money money;

  @Test
  public void hardcodedToUSD() {
    givenMoneyWithAmount(anyAmount);

    thenCurrencyIs(money, "USD");
  }

  @Test
  public void returnsAmount() {
    givenMoneyWithAmount(anyAmount);

    thenAmountIs(anyAmount);
  }

  private void givenMoneyWithAmount(String amount) {
    money = new Money(new BigDecimal(amount));
  }

  private void thenCurrencyIs(Money money, String expectedCurrency) {
    assertEquals(Currency.getInstance(expectedCurrency), money.getCurrency());
  }

  private void thenAmountIs(String expectedAmount) {
    assertEquals(new BigDecimal(expectedAmount), money.getAmount());
  }
}
