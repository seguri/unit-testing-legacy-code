package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetTopicFixture.assetTopic;
import static com.assetco.hotspots.optimization.fixture.AssetTopicFixture.emptyAssetTopics;
import static com.assetco.hotspots.optimization.fixture.AssetVendorFixture.assetVendor;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Basic;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Partner;
import static com.assetco.search.results.HotspotKey.Highlight;
import static com.assetco.search.results.HotspotKey.Showcase;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetTopic;
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
  void prevailingPartnerReceivesFirstFiveItemsInShowcase() {
    var partnerVendor = assetVendor(Partner);
    var otherPartnerVendor = assetVendor(Partner);
    var topics = emptyAssetTopics();
    var expected = assetsInSearchResults(partnerVendor, topics, 4);
    expected.add(assetInSearchResults(partnerVendor, topics));
    assetInSearchResults(otherPartnerVendor, topics);

    whenOptimize();

    thenHotspotHasExactly(Showcase, expected);
  }

  @Test
  void allItemsDeservingHighlightAreHighlighted() {
    var basicVendor = assetVendor(Basic);
    var partnerVendor = assetVendor(Partner);
    var hotTopic = assetTopic();
    var topic = assetTopic();
    optimizer.setHotTopics(() -> List.of(hotTopic, topic));
    var expected = assetsInSearchResults(basicVendor, topic, 2);
    assetsInSearchResults(basicVendor, hotTopic, 3);
    expected.add(assetInSearchResults(basicVendor, topic));

    whenOptimize();

    thenHotspotHas(Highlight, expected);
  }

  private Asset assetInSearchResults(AssetVendor vendor, AssetTopic topic) {
    return assetInSearchResults(vendor, List.of(topic));
  }

  private Asset assetInSearchResults(AssetVendor vendor, List<AssetTopic> topics) {
    var asset = asset(vendor, topics);
    searchResults.addFound(asset);
    return asset;
  }

  private List<Asset> assetsInSearchResults(AssetVendor vendor, AssetTopic topic, int count) {
    return assetsInSearchResults(vendor, List.of(topic), count);
  }

  private List<Asset> assetsInSearchResults(
      AssetVendor vendor, List<AssetTopic> topics, int count) {
    return IntStream.range(0, count)
        .mapToObj(i -> assetInSearchResults(vendor, topics))
        .collect(Collectors.toList());
  }

  private void whenOptimize() {
    optimizer.optimize(searchResults);
  }

  private void thenHotspotHasExactly(HotspotKey key, List<Asset> expected) {
    var members = searchResults.getHotspot(key).getMembers().toArray(new Asset[0]);
    assertArrayEquals(expected.toArray(new Asset[0]), members);
  }

  private void thenHotspotHas(HotspotKey key, List<Asset> expected) {
    var members = searchResults.getHotspot(key).getMembers();
    for (Asset asset : expected) {
      assertTrue(members.contains(asset));
    }
  }
}
