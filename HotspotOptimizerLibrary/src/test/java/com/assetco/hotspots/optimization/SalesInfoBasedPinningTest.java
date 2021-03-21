package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static com.assetco.search.results.HotspotKey.HighValue;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SalesInfoBasedPinningTest extends AbstractOptimizerTest {

  private static Stream<Arguments> highValueDetectionData() {
    return Stream.of(
        Arguments.of(0, "0", "5000.00", 1),
        Arguments.of(0, "2500", "5000.00", 0),
        Arguments.of(0, "500", "5000.00", 1),
        Arguments.of(0, "1250", "5000.00", 0),
        Arguments.of(0, "1000.1", "5000.00", 0),
        Arguments.of(0, "1000", "5000.00", 1),
        Arguments.of(0, "100", "500.00", 0),
        Arguments.of(1, "100", "500.00", 1),
        Arguments.of(2, "100", "500.00", 2));
  }

  private static Stream<Arguments> goodShortTermTrafficDetectionData() {
    return Stream.of(
        Arguments.of(0, 0, 0, 0),
        Arguments.of(0, 10000, 10000, 1),
        Arguments.of(1, 10000, 10000, 2),
        Arguments.of(0, 5000, 5000, 1),
        Arguments.of(0, 1000, 1000, 1),
        Arguments.of(0, 999, 999, 0),
        Arguments.of(0, 1000, 999, 1),
        Arguments.of(0, 1000, 20, 1),
        Arguments.of(0, 1000, 10, 1),
        Arguments.of(0, 1000, 5, 1),
        Arguments.of(0, 1000, 4, 0),
        Arguments.of(0, 10000, 49, 0));
  }

  private static Stream<Arguments> goodLongTermTrafficDetectionData() {
    return Stream.of(
        Arguments.of(0, 0, 0, 0),
        Arguments.of(0, 10000, 10000, 0),
        Arguments.of(0, 50000, 50000, 1),
        Arguments.of(0, 50000, 10000, 1),
        Arguments.of(0, 50000, 900, 1),
        Arguments.of(0, 50000, 500, 1),
        Arguments.of(0, 50000, 250, 0),
        Arguments.of(0, 50000, 499, 1),
        Arguments.of(0, 50000, 399, 0),
        Arguments.of(0, 50000, 400, 1),
        Arguments.of(1, 50000, 400, 2),
        Arguments.of(1, 50000, 5000, 2));
  }

  @ParameterizedTest
  @MethodSource("highValueDetectionData")
  void highValueDetection(int partnerSize, String royalties, String revenue, int highValueSize) {
    assetsInSearchResults(partnerSize, () -> asset(PARTNER_VENDOR));
    var asset = asset(BASIC_VENDOR, assetPurchaseInfo(revenue, royalties), assetPurchaseInfo());
    searchResults.addFound(asset);
    dealEligibility.put(asset, false);

    whenOptimize();

    thenHotspotCountIs(HighValue, highValueSize);
  }

  @ParameterizedTest
  @MethodSource("goodShortTermTrafficDetectionData")
  void goodShortTermTrafficDetection(int partnerSize, int shown, int purchased, int highValueSize) {
    assetsInSearchResults(partnerSize, () -> asset(PARTNER_VENDOR));
    assetInSearchResults(
        asset(BASIC_VENDOR, assetPurchaseInfo(), assetPurchaseInfo(shown, purchased)));

    whenOptimize();

    thenHotspotCountIs(HighValue, highValueSize);
  }

  @ParameterizedTest
  @MethodSource("goodLongTermTrafficDetectionData")
  void goodLongTermTrafficDetection(int partnerSize, int shown, int purchased, int highValueSize) {
    assetsInSearchResults(partnerSize, () -> asset(PARTNER_VENDOR));
    assetInSearchResults(
        asset(BASIC_VENDOR, assetPurchaseInfo(shown, purchased), assetPurchaseInfo()));

    whenOptimize();

    thenHotspotCountIs(HighValue, highValueSize);
  }
}
