package com.assetco.hotspots.optimization.fixture;

import static com.assetco.hotspots.optimization.fixture.MoneyFixture.money;

import com.assetco.search.results.AssetPurchaseInfo;
import java.util.Random;

public class AssetPurchaseInfoFixture {

  private static final Random RANDOM = new Random();

  public static AssetPurchaseInfo assetPurchaseInfo() {
    var timesShown = RANDOM.nextLong();
    var timesPurchased = RANDOM.nextLong();
    var totalRevenue = money();
    var totalRoyaltiesOwed = money();
    return new AssetPurchaseInfo(timesShown, timesPurchased, totalRevenue, totalRoyaltiesOwed);
  }
}
