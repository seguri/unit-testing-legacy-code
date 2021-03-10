package com.assetco.hotspots.optimization.fixture;

import com.assetco.search.results.Money;
import java.math.BigDecimal;

public class MoneyFixture {

  public static Money money() {
    var randomAmount = BigDecimal.valueOf(Math.random());
    return new Money(randomAmount);
  }

}
