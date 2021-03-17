package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static com.assetco.hotspots.optimization.fixture.AssetVendorFixture.assetVendor;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Partner;
import static com.assetco.search.results.HotspotKey.Deals;

import org.junit.jupiter.api.Test;

class DealsPinningTest extends AbstractOptimizerTest {

  @Test
  void pinningTest1() {
    var partnerVendor = assetVendor(Partner);
    var purchaseInfo = assetPurchaseInfo(0, 0, 1000, 0);
    var asset = asset(partnerVendor, purchaseInfo, assetPurchaseInfo());
    searchResults.addFound(asset);

    sut.optimize(searchResults);

    thenHotspotHasExactly(Deals, asset);
  }
}
