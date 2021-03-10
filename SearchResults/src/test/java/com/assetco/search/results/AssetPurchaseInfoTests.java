package com.assetco.search.results;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AssetPurchaseInfoTests {

  @Test
  public void storesAndReturnsValues() {
    var timesShown = Any.anyLong();
    long timesPurchased = Any.anyLong();
    var totalRevenue = Any.money();
    var royaltiesOwed = Any.money();

    var info = new AssetPurchaseInfo(timesShown, timesPurchased, totalRevenue, royaltiesOwed);

    assertEquals(timesShown, info.getTimesShown());
    assertEquals(timesPurchased, info.getTimesPurchased());
    assertEquals(totalRevenue, info.getTotalRevenue());
    assertEquals(royaltiesOwed, info.getTotalRoyaltiesOwed());
  }
}
