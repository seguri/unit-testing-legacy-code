package com.assetco.hotspots.optimization.fixture;

import static com.assetco.hotspots.optimization.fixture.MoneyFixture.money;

import com.assetco.search.results.AssetPurchaseInfo;

public class AssetPurchaseInfoFixture {

  public static AssetPurchaseInfo assetPurchaseInfo(
      long timesShown, long timesPurchased, String totalRevenue, String totalRoyaltiesOwed) {
    return new AssetPurchaseInfo(
        timesShown, timesPurchased, money(totalRevenue), money(totalRoyaltiesOwed));
  }

  public static AssetPurchaseInfo assetPurchaseInfo(
      long timesShown, long timesPurchased, double totalRevenue, double totalRoyaltiesOwed) {
    return new AssetPurchaseInfo(
        timesShown, timesPurchased, money(totalRevenue), money(totalRoyaltiesOwed));
  }

  public static AssetPurchaseInfo assetPurchaseInfo(
      String totalRevenue, String totalRoyaltiesOwed) {
    return assetPurchaseInfo(0, 0, totalRevenue, totalRoyaltiesOwed);
  }

  public static AssetPurchaseInfo assetPurchaseInfo(
      double totalRevenue, double totalRoyaltiesOwed) {
    return assetPurchaseInfo(0, 0, totalRevenue, totalRoyaltiesOwed);
  }

  public static AssetPurchaseInfo assetPurchaseInfo(long timesShown, long timesPurchased) {
    return assetPurchaseInfo(timesShown, timesPurchased, 0, 0);
  }

  public static AssetPurchaseInfo assetPurchaseInfo() {
    return assetPurchaseInfo(0, 0, 0, 0);
  }
}
