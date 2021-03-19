package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static com.assetco.search.results.HotspotKey.Deals;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetVendor;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DealsPinningTest extends AbstractOptimizerTest {

  private static Stream<Arguments> singleAssetArguments() {
    return Stream.of(
        Arguments.of(partnerAsset(assetPurchaseInfo(1000.0, 0.0)), true, true),
        Arguments.of(partnerAsset(assetPurchaseInfo(1000.0, 0.0)), false, true),
        Arguments.of(partnerAsset(assetPurchaseInfo(1000.0, 200.0)), true, true),
        Arguments.of(partnerAsset(assetPurchaseInfo(1000.0, 600.0)), true, true),
        Arguments.of(partnerAsset(assetPurchaseInfo(1000.0, 999.99)), true, false),
        Arguments.of(partnerAsset(assetPurchaseInfo(1000.0, 700.0)), true, true),
        Arguments.of(partnerAsset(assetPurchaseInfo(1000.0, 700.001)), true, false),
        Arguments.of(partnerAsset(assetPurchaseInfo(10.0, 7.0)), true, true));
  }

  private static Stream<Arguments> highPayoutAssetArguments() {
    return Stream.of(
        Arguments.of(
            highPayoutAsset(PARTNER_VENDOR),
            asset(GOLD_VENDOR, assetPurchaseInfo(1000.0, 0.0), assetPurchaseInfo()),
            true,
            true),
        Arguments.of(
            highPayoutAsset(PARTNER_VENDOR),
            asset(GOLD_VENDOR, assetPurchaseInfo(1000.0, 700.0), assetPurchaseInfo()),
            true,
            false),
        Arguments.of(
            highPayoutAsset(PARTNER_VENDOR),
            asset(GOLD_VENDOR, assetPurchaseInfo(1000.0, 500.0), assetPurchaseInfo()),
            true,
            true),
        Arguments.of(
            highPayoutAsset(PARTNER_VENDOR),
            asset(GOLD_VENDOR, assetPurchaseInfo(1000.0, 500.01), assetPurchaseInfo()),
            true,
            false),
        Arguments.of(
            highPayoutAsset(PARTNER_VENDOR),
            asset(SILVER_VENDOR, assetPurchaseInfo(100_000.0, 500.01), assetPurchaseInfo()),
            true,
            true),
        Arguments.of(
            highPayoutAsset(PARTNER_VENDOR),
            asset(SILVER_VENDOR, assetPurchaseInfo(100_000.0, 5000.0), assetPurchaseInfo()),
            true,
            true),
        Arguments.of(
            highPayoutAsset(PARTNER_VENDOR),
            asset(SILVER_VENDOR, assetPurchaseInfo(100_000.0, 0.0), assetPurchaseInfo()),
            true,
            true));
  }

  private static Asset highPayoutAsset(AssetVendor vendor) {
    return asset(vendor, assetPurchaseInfo(1_000_000.0, 0.0), assetPurchaseInfo());
  }

  @ParameterizedTest
  @MethodSource("singleAssetArguments")
  public void singleAsset(Asset candidate, boolean eligible, boolean included) {
    searchResults.addFound(candidate);
    dealEligibility.put(candidate, eligible);

    sut.optimize(searchResults);

    thenAssetAdded(candidate, included);
  }

  @ParameterizedTest
  @MethodSource("highPayoutAssetArguments")
  public void highPayoutLowerGradeAsset(
      Asset highPayout, Asset candidate, boolean eligible, boolean included) {
    searchResults.addFound(highPayout);
    dealEligibility.put(highPayout, true);

    searchResults.addFound(candidate);
    dealEligibility.put(candidate, eligible);

    sut.optimize(searchResults);

    thenHotspotHas(Deals, highPayout);
    thenAssetAdded(candidate, included);
  }

  @Test
  public void lowPayoutLowerGradeAssetCases() {}

  private void thenAssetAdded(Asset candidate, boolean shouldBeAdded) {
    if (shouldBeAdded) {
      thenHotspotHas(Deals, candidate);
    } else {
      thenHotspotHasNot(Deals, candidate);
    }
  }
}
