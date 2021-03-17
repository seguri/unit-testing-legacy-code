package com.assetco.hotspots.optimization;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.assetco.search.results.Asset;
import com.assetco.search.results.HotspotKey;
import com.assetco.search.results.SearchResults;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

abstract class AbstractOptimizerTest {

  protected SearchResults searchResults;
  protected SearchResultHotspotOptimizer sut;

  @BeforeEach
  void setup() {
    searchResults = new SearchResults();
    sut = new SearchResultHotspotOptimizer();
  }

  protected void thenHotspotHas(HotspotKey key, List<Asset> expected) {
    var members = searchResults.getHotspot(key).getMembers();
    for (Asset asset : expected) {
      assertTrue(members.contains(asset));
    }
  }

  protected void thenHotspotHasExactly(HotspotKey key, Asset... expected) {
    var members = searchResults.getHotspot(key).getMembers();
    assertArrayEquals(expected, members.toArray(new Asset[0]));
  }

  protected void thenHotspotHasExactly(HotspotKey key, List<Asset> expected) {
    thenHotspotHasExactly(key, expected.toArray(new Asset[0]));
  }
}
