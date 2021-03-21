package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetTopicFixture.assetTopic;
import static com.assetco.search.results.HotspotKey.Showcase;
import static com.assetco.search.results.HotspotKey.TopPicks;

import com.assetco.search.results.AssetTopic;
import com.assetco.search.results.AssetVendor;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TopicsBasedPinningTest extends AbstractOptimizerTest {

  private static Stream<Arguments> topicsToShowcaseMappingsData() {
    return Stream.of(
        Arguments.of(0, 0, 0, 0, BASIC_VENDOR, 0, 0, 0),
        Arguments.of(1, 2, 3, 0, BASIC_VENDOR, 0, 1, 0),
        Arguments.of(3, 2, 3, 0, BASIC_VENDOR, 0, 3, 0),
        Arguments.of(5, 2, 3, 0, BASIC_VENDOR, 0, 5, 5),
        Arguments.of(6, 2, 3, 0, BASIC_VENDOR, 0, 5, 6),
        Arguments.of(2, 2, 2, 1, BASIC_VENDOR, 0, 3, 0),
        Arguments.of(0, 2, 2, 1, BASIC_VENDOR, 0, 2, 0),
        Arguments.of(0, 1, 1, 3, BASIC_VENDOR, 0, 3, 1),
        Arguments.of(0, 2, 2, 3, BASIC_VENDOR, 0, 2, 0),
        Arguments.of(6, 2, 3, 0, PARTNER_VENDOR, 1, 5, 6),
        Arguments.of(6, 2, 3, 0, PARTNER_VENDOR, 5, 5, 6),
        Arguments.of(0, 2, 3, 0, PARTNER_VENDOR, 5, 2, 0),
        Arguments.of(0, 2, 0, 0, PARTNER_VENDOR, 5, 2, 0),
        Arguments.of(1, 2, 0, 0, PARTNER_VENDOR, 5, 1, 0),
        Arguments.of(0, 0, 0, 0, PARTNER_VENDOR, 5, 5, 0),
        Arguments.of(0, 0, 0, 0, PARTNER_VENDOR, 4, 4, 0),
        Arguments.of(0, 0, 0, 0, GOLD_VENDOR, 4, 0, 0));
  }

  @ParameterizedTest
  @MethodSource("topicsToShowcaseMappingsData")
  void topicsToShowcaseMappings(
      int topic1Size,
      int topic2Size,
      int topic3Size,
      int additionalTopic1Size,
      AssetVendor initialAssetVendor,
      int initialAssetSize,
      int showcaseSize,
      int topPicksSize) {
    var topic1 = assetTopic();
    var topic2 = assetTopic();
    var topic3 = assetTopic();
    hotTopics(topic1, topic2, topic3);
    assetsInSearchResults(initialAssetSize, () -> asset(initialAssetVendor));
    assetsInSearchResults(topic1Size, () -> asset(BASIC_VENDOR, topic1));
    assetsInSearchResults(topic2Size, () -> asset(BASIC_VENDOR, topic2));
    assetsInSearchResults(topic3Size, () -> asset(BASIC_VENDOR, topic3));
    assetsInSearchResults(additionalTopic1Size, () -> asset(BASIC_VENDOR, topic1));

    whenOptimize();

    thenHotspotCountIs(Showcase, showcaseSize);
    thenHotspotCountIs(TopPicks, topPicksSize);
  }

  @Test
  void canHandleNullTopics() {
    hotTopics((AssetTopic) null);
    assetInSearchResults(asset(BASIC_VENDOR, null));

    whenOptimize();
  }
}
