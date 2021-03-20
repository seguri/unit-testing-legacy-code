package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetVendorFixture.assetVendor;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Basic;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Gold;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Partner;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Silver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetVendor;
import com.assetco.search.results.HotspotKey;
import com.assetco.search.results.SearchResults;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;

abstract class AbstractOptimizerTest {
  protected static final AssetVendor PARTNER_VENDOR = assetVendor(Partner);
  protected static final AssetVendor OTHER_PARTNER_VENDOR = assetVendor(Partner);
  protected static final AssetVendor GOLD_VENDOR = assetVendor(Gold);
  protected static final AssetVendor SILVER_VENDOR = assetVendor(Silver);
  protected static final AssetVendor BASIC_VENDOR = assetVendor(Basic);

  protected Map<Asset, Boolean> dealEligibility;
  protected SearchResults searchResults;
  protected SearchResultHotspotOptimizer sut;

  @BeforeEach
  void setup() {
    dealEligibility = new HashMap<>();
    searchResults = new SearchResults();
    sut = new SearchResultHotspotOptimizer();
  }

  protected Asset assetInSearchResults(Asset asset) {
    searchResults.addFound(asset);
    return asset;
  }

  protected Collection<Asset> assetsInSearchResults(int count, Supplier<Asset> generator) {
    return IntStream.range(0, count)
        .mapToObj(i -> assetInSearchResults(generator.get()))
        .collect(Collectors.toList());
  }

  protected void whenOptimize() {
    sut.optimize(searchResults);
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

  protected void thenHotspotCountIs(HotspotKey key, int count) {
    assertEquals(count, searchResults.getHotspot(key).getMembers().size(), key.toString());
  }
}
