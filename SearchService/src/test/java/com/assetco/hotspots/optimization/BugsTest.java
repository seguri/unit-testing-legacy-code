package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static com.assetco.hotspots.optimization.fixture.AssetTopicFixture.assetTopic;
import static com.assetco.hotspots.optimization.fixture.AssetTopicFixture.emptyAssetTopics;
import static com.assetco.hotspots.optimization.fixture.AssetVendorFixture.assetVendor;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Basic;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Partner;
import static com.assetco.search.results.HotspotKey.HighValue;
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
  private SearchResultHotspotOptimizer sut;

  @BeforeEach
  void setup() {
    searchResults = new SearchResults();
    sut = new SearchResultHotspotOptimizer();
  }

  @Test
  void prevailingPartnerReceivesFirstFiveItemsInShowcase() {
    var partnerVendor = assetVendor(Partner);
    var otherPartnerVendor = assetVendor(Partner);
    var expected = assetsInSearchResults(partnerVendor, 4);
    expected.add(assetInSearchResults(partnerVendor));
    assetInSearchResults(otherPartnerVendor);

    sut.optimize(searchResults);

    thenHotspotHasExactly(Showcase, expected);
  }

  @Test
  void allItemsDeservingHighlightAreHighlighted() {
    var basicVendor = assetVendor(Basic);
    var hotTopic = assetTopic();
    var topic = assetTopic();
    sut.setHotTopics(() -> List.of(hotTopic, topic));
    var expected = assetsInSearchResults(basicVendor, topic, 2);
    assetsInSearchResults(basicVendor, hotTopic, 3);
    expected.add(assetInSearchResults(basicVendor, topic));

    sut.optimize(searchResults);

    thenHotspotHas(Highlight, expected);
  }

  @Test
  void itemsWithHighRecentAndLastMonthVolumeSingleEntered() {
    var basicVendor = assetVendor(Basic);
    var purchaseInfoLast30Days = assetPurchaseInfo(50000, 50000);
    var purchaseInfoLast24Hours = assetPurchaseInfo(4000, 4000);
    var asset = asset(basicVendor, purchaseInfoLast30Days, purchaseInfoLast24Hours);
    searchResults.addFound(asset);

    sut.optimize(searchResults);

    thenHotspotHasExactly(HighValue, List.of(asset));
  }

  private Asset assetInSearchResults(AssetVendor vendor) {
    return assetInSearchResults(vendor, emptyAssetTopics());
  }

  private Asset assetInSearchResults(AssetVendor vendor, AssetTopic topic) {
    return assetInSearchResults(vendor, List.of(topic));
  }

  private Asset assetInSearchResults(AssetVendor vendor, List<AssetTopic> topics) {
    var asset = asset(vendor, topics);
    searchResults.addFound(asset);
    return asset;
  }

  private List<Asset> assetsInSearchResults(AssetVendor vendor, int count) {
    return assetsInSearchResults(vendor, emptyAssetTopics(), count);
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
