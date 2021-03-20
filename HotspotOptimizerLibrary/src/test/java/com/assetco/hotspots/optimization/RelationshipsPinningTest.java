package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.search.results.HotspotKey.Deals;
import static com.assetco.search.results.HotspotKey.Fold;
import static com.assetco.search.results.HotspotKey.HighValue;
import static com.assetco.search.results.HotspotKey.Highlight;
import static com.assetco.search.results.HotspotKey.Showcase;
import static com.assetco.search.results.HotspotKey.TopPicks;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RelationshipsPinningTest extends AbstractOptimizerTest {

  private static Stream<Arguments> countData() {
    return Stream.of(
        Arguments.of(new CountData(1, 0, 0, 1, 1, 1, 0, 0, 3, 2, 1)),
        Arguments.of(new CountData(2, 0, 0, 1, 1, 1, 0, 0, 4, 3, 2)),
        Arguments.of(new CountData(2, 2, 0, 1, 1, 1, 0, 0, 6, 5, 4)),
        Arguments.of(new CountData(2, 2, 1, 1, 1, 1, 3, 0, 7, 6, 5)),
        Arguments.of(new CountData(2, 4, 1, 1, 1, 1, 4, 0, 9, 8, 7)),
        Arguments.of(new CountData(3, 0, 0, 1, 1, 1, 3, 0, 5, 4, 3)),
        Arguments.of(new CountData(3, 1, 0, 1, 1, 1, 3, 0, 6, 5, 4)),
        Arguments.of(new CountData(3, 1, 2, 1, 1, 1, 5, 0, 8, 7, 6)),
        Arguments.of(new CountData(5, 0, 0, 1, 1, 1, 5, 0, 7, 6, 5)),
        Arguments.of(new CountData(5, 0, 0, 5, 5, 5, 5, 0, 15, 10, 5)),
        Arguments.of(new CountData(6, 0, 0, 5, 5, 5, 5, 1, 16, 11, 6)),
        Arguments.of(new CountData(6, 0, 0, 10, 10, 10, 5, 1, 26, 16, 6)),
        Arguments.of(new CountData(10, 0, 0, 10, 10, 10, 5, 5, 30, 20, 10)));
  }

  @ParameterizedTest
  @MethodSource("countData")
  void assetCountsInAndOut(CountData data) {
    assetsInSearchResults(data.partnerSize, () -> asset(PARTNER_VENDOR));
    assetsInSearchResults(data.otherPartnerSize, () -> asset(OTHER_PARTNER_VENDOR));
    assetsInSearchResults(data.additionalPartnerSize, () -> asset(PARTNER_VENDOR));
    assetsInSearchResults(data.goldSize, () -> asset(GOLD_VENDOR));
    assetsInSearchResults(data.silverSize, () -> asset(SILVER_VENDOR));
    assetsInSearchResults(data.basicSize, () -> asset(BASIC_VENDOR));

    whenOptimize();

    thenHotspotCountIs(Showcase, data.showcaseSize);
    thenHotspotCountIs(TopPicks, data.topPicksSize);
    thenHotspotCountIs(Fold, data.foldSize);
    thenHotspotCountIs(HighValue, data.highValueSize);
    thenHotspotCountIs(Deals, data.dealsSize);
    thenHotspotCountIs(Highlight, 0);
  }

  private static class CountData {

    /** Number of assets of Partner vendor */
    private final int partnerSize;

    /** Number of assets of another Partner vendor to be inserted after previous assets */
    private final int otherPartnerSize;

    /** Number of assets of Partner vendor to be inserted after previous assets */
    private final int additionalPartnerSize;

    private final int goldSize;
    private final int silverSize;
    private final int basicSize;
    private final int showcaseSize;
    private final int topPicksSize;
    private final int foldSize;
    private final int highValueSize;
    private final int dealsSize;

    public CountData(
        int partnerSize,
        int otherPartnerSize,
        int additionalPartnerSize,
        int goldSize,
        int silverSize,
        int basicSize,
        int showcaseSize,
        int topPicksSize,
        int foldSize,
        int highValueSize,
        int dealsSize) {
      this.partnerSize = partnerSize;
      this.otherPartnerSize = otherPartnerSize;
      this.additionalPartnerSize = additionalPartnerSize;
      this.goldSize = goldSize;
      this.silverSize = silverSize;
      this.basicSize = basicSize;
      this.showcaseSize = showcaseSize;
      this.topPicksSize = topPicksSize;
      this.foldSize = foldSize;
      this.highValueSize = highValueSize;
      this.dealsSize = dealsSize;
    }
  }
}
