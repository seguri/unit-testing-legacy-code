package com.assetco.hotspots.optimization.fixture;

import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static java.util.Collections.singletonList;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetPurchaseInfo;
import com.assetco.search.results.AssetTopic;
import com.assetco.search.results.AssetVendor;
import java.util.List;

public class AssetFixture {

  public static Asset asset(AssetVendor vendor) {
    return asset(vendor, assetPurchaseInfo(), assetPurchaseInfo(), List.of());
  }

  public static Asset asset(AssetVendor vendor, AssetTopic topic) {
    return asset(vendor, assetPurchaseInfo(), assetPurchaseInfo(), singletonList(topic));
  }

  public static Asset asset(
      AssetVendor vendor,
      AssetPurchaseInfo purchaseInfoLast30Days,
      AssetPurchaseInfo purchaseInfoLast24Hours) {
    return asset(vendor, purchaseInfoLast30Days, purchaseInfoLast24Hours, List.of());
  }

  public static Asset asset(
      AssetVendor vendor,
      AssetPurchaseInfo purchaseInfoLast30Days,
      AssetPurchaseInfo purchaseInfoLast24Hours,
      List<AssetTopic> topics) {
    return new Asset(
        "ID", "TITLE", null, null, purchaseInfoLast30Days, purchaseInfoLast24Hours, topics, vendor);
  }
}
