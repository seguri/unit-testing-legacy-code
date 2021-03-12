package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetVendorFixture.assetVendor;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Partner;
import static com.assetco.search.results.HotspotKey.Showcase;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetVendor;
import com.assetco.search.results.HotspotKey;
import com.assetco.search.results.SearchResults;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BugsTest {

  private SearchResults searchResults;
  private SearchResultHotspotOptimizer optimizer;

  @BeforeEach
  void setup() {
    searchResults = new SearchResults();
    optimizer = new SearchResultHotspotOptimizer();
  }

  @Test
  void precedingPartnerWithLongTrailingAssetsDoesNotWin() {
    var partnerVendor = assetVendor(Partner);
    var otherPartnerVendor = assetVendor(Partner);
    var expected = givenAssetsInResultsWithVendor(partnerVendor, 4);
    expected.add(givenAssetInResultsWithVendor(partnerVendor));
    givenAssetInResultsWithVendor(otherPartnerVendor);

    whenOptimize();

    thenHotspotHasExactly(Showcase, expected);
  }

  private Asset givenAssetInResultsWithVendor(AssetVendor assetVendor) {
    var asset = asset(assetVendor);
    searchResults.addFound(asset);
    return asset;
  }

  private List<Asset> givenAssetsInResultsWithVendor(AssetVendor assetVendor, int count) {
    return IntStream.range(0, count)
        .mapToObj(i -> givenAssetInResultsWithVendor(assetVendor))
        .collect(Collectors.toList());
  }

  private void whenOptimize() {
    optimizer.optimize(searchResults);
  }

  private void thenHotspotHasExactly(HotspotKey key, List<Asset> expected) {
    var members = searchResults.getHotspot(key).getMembers().toArray(new Asset[0]);
    assertArrayEquals(expected.toArray(new Asset[0]), members);
  }
}
