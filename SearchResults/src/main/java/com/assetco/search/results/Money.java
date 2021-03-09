package com.assetco.search.results;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class Money {
  private final BigDecimal amount;

  public Money(BigDecimal amount) {
    this.amount = amount;
  }

  public Currency getCurrency() {
    return Currency.getInstance(Locale.US);
  }

  public BigDecimal getAmount() {
    return amount;
  }
}
