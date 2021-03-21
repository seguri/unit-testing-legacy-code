package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static com.assetco.hotspots.optimization.fixture.AssetTopicFixture.assetTopic;
import static com.assetco.hotspots.optimization.fixture.AssetVendorFixture.assetVendor;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Basic;
import static com.assetco.search.results.HotspotKey.HighValue;
import static com.assetco.search.results.HotspotKey.Highlight;
import static com.assetco.search.results.HotspotKey.Showcase;

import com.assetco.search.results.Asset;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class BugsTest extends AbstractOptimizerTest {

  @Test
  void prevailingPartnerReceivesFirstFiveItemsInShowcase() {
    var expected = new ArrayList<Asset>();
    expected.add(assetInSearchResults(asset(PARTNER_VENDOR)));
    assetInSearchResults(asset(OTHER_PARTNER_VENDOR));
    expected.addAll(assetsInSearchResults(4, () -> asset(PARTNER_VENDOR)));

    whenOptimize();

    thenHotspotHasExactly(Showcase, expected);
  }

  @Test
  void allItemsDeservingHighlightAreHighlighted() {
    var hotTopic = assetTopic();
    var topic = assetTopic();
    hotTopics(hotTopic, topic);
    var expected = assetsInSearchResults(2, () -> asset(BASIC_VENDOR, topic));
    assetsInSearchResults(3, () -> asset(BASIC_VENDOR, hotTopic));
    expected.add(assetInSearchResults(asset(BASIC_VENDOR, topic)));

    whenOptimize();

    thenHotspotHas(Highlight, expected);
  }

  @Test
  void itemsWithHighRecentAndLastMonthVolumeSingleEntered() {
    var basicVendor = assetVendor(Basic);
    var purchaseInfoLast30Days = assetPurchaseInfo(50000, 50000);
    var purchaseInfoLast24Hours = assetPurchaseInfo(4000, 4000);
    var asset =
        assetInSearchResults(asset(basicVendor, purchaseInfoLast30Days, purchaseInfoLast24Hours));

    whenOptimize();

    thenHotspotHasExactly(HighValue, asset);
  }
}
