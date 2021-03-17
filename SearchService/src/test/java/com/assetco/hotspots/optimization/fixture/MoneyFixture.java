package com.assetco.hotspots.optimization.fixture;

import com.assetco.search.results.Money;
import java.math.BigDecimal;

public class MoneyFixture {

  public static Money money(double amount) {
    return new Money(BigDecimal.valueOf(amount));
  }

  public static Money money(String amount) {
    return new Money(new BigDecimal(amount));
  }
}
