package com.assetco.hotspots.optimization.fixture;

import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static java.util.Collections.emptyList;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetTopic;
import com.assetco.search.results.AssetVendor;
import java.util.List;

public class AssetFixture {

  public static Asset asset(AssetVendor vendor) {
    return asset(vendor, emptyList());
  }

  public static Asset asset(AssetVendor vendor, List<AssetTopic> topics) {
    var purchaseInfoLast30Days = assetPurchaseInfo();
    var purchaseInfoLast24Hours = assetPurchaseInfo();
    return new Asset(
        "ID", "TITLE", null, null, purchaseInfoLast30Days, purchaseInfoLast24Hours, topics, vendor);
  }
}
