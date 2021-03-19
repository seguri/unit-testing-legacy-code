package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static com.assetco.hotspots.optimization.fixture.AssetVendorFixture.assetVendor;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Basic;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Gold;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Partner;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Silver;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetPurchaseInfo;
import com.assetco.search.results.AssetVendor;
import com.assetco.search.results.HotspotKey;
import com.assetco.search.results.SearchResults;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;

abstract class AbstractOptimizerTest {
  protected static final AssetVendor PARTNER_VENDOR = assetVendor(Partner);
  protected static final AssetVendor GOLD_VENDOR = assetVendor(Gold);
  protected static final AssetVendor SILVER_VENDOR = assetVendor(Silver);
  protected static final AssetVendor BASIC_VENDOR = assetVendor(Basic);

  protected Map<Asset, Boolean> dealEligibility;
  protected SearchResults searchResults;
  protected SearchResultHotspotOptimizer sut;

  protected static Asset partnerAsset(AssetPurchaseInfo purchaseInfoLast30Days) {
    return asset(PARTNER_VENDOR, purchaseInfoLast30Days, assetPurchaseInfo());
  }

  @BeforeEach
  void setup() {
    dealEligibility = new HashMap<>();
    searchResults = new SearchResults();
    sut = new SearchResultHotspotOptimizer();
  }

  protected void thenHotspotHas(HotspotKey key, Asset... expected) {
    thenHotspotHas(key, Arrays.asList(expected));
  }

  protected void thenHotspotHas(HotspotKey key, List<Asset> expected) {
    var members = searchResults.getHotspot(key).getMembers();
    for (Asset asset : expected) {
      assertTrue(members.contains(asset));
    }
  }

  protected void thenHotspotHasNot(HotspotKey key, Asset... forbidden) {
    thenHotspotHasNot(key, Arrays.asList(forbidden));
  }

  protected void thenHotspotHasNot(HotspotKey key, List<Asset> forbidden) {
    var members = searchResults.getHotspot(key).getMembers();
    for (var asset : forbidden) {
      assertFalse(members.contains(asset));
    }
  }

  protected void thenHotspotHasExactly(HotspotKey key, Asset... expected) {
    thenHotspotHasExactly(key, Arrays.asList(expected));
  }

  protected void thenHotspotHasExactly(HotspotKey key, List<Asset> expected) {
    var members = searchResults.getHotspot(key).getMembers();
    assertIterableEquals(expected, members);
  }
}
