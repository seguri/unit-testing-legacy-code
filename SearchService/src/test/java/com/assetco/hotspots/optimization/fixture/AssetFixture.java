package com.assetco.hotspots.optimization.fixture;

import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static com.assetco.hotspots.optimization.fixture.AssetTopicFixture.emptyAssetTopics;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetPurchaseInfo;
import com.assetco.search.results.AssetTopic;
import com.assetco.search.results.AssetVendor;
import java.util.List;

public class AssetFixture {

  public static Asset asset(AssetVendor vendor) {
    return asset(vendor, assetPurchaseInfo(), assetPurchaseInfo(), emptyAssetTopics());
  }

  public static Asset asset(AssetVendor vendor, List<AssetTopic> topics) {
    return asset(vendor, assetPurchaseInfo(), assetPurchaseInfo(), topics);
  }

  public static Asset asset(
      AssetVendor vendor,
      AssetPurchaseInfo purchaseInfoLast30Days,
      AssetPurchaseInfo purchaseInfoLast24Hours) {
    return asset(vendor, purchaseInfoLast30Days, purchaseInfoLast24Hours, emptyAssetTopics());
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
