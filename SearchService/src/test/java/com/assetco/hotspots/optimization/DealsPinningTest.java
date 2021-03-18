package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static com.assetco.search.results.HotspotKey.Deals;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetVendor;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DealsPinningTest extends AbstractOptimizerTest {

  private static List<SingleAssetCaseData> singleAssetCaseData() {
    return List.of(
        new SingleAssetCaseData(PARTNER_VENDOR, 1000.0, 0.0, true, true),
        new SingleAssetCaseData(PARTNER_VENDOR, 1000.0, 0.0, false, true),
        new SingleAssetCaseData(PARTNER_VENDOR, 1000.0, 200.0, true, true),
        new SingleAssetCaseData(PARTNER_VENDOR, 1000.0, 600.0, true, true),
        new SingleAssetCaseData(PARTNER_VENDOR, 1000.0, 999.99, true, false),
        new SingleAssetCaseData(PARTNER_VENDOR, 1000.0, 700.0, true, true),
        new SingleAssetCaseData(PARTNER_VENDOR, 1000.0, 700.001, true, false),
        new SingleAssetCaseData(PARTNER_VENDOR, 10.0, 7.0, true, true));
  }

  private static List<HighPayoutCaseData> highPayoutCaseData() {
    return List.of(
        new HighPayoutCaseData(PARTNER_VENDOR, GOLD_VENDOR, 1000.0, 0.0, true, true),
        new HighPayoutCaseData(PARTNER_VENDOR, GOLD_VENDOR, 1000.0, 0.0, false, false),
        new HighPayoutCaseData(PARTNER_VENDOR, GOLD_VENDOR, 1000.0, 700.0, true, false),
        new HighPayoutCaseData(PARTNER_VENDOR, GOLD_VENDOR, 1000.0, 500.0, true, true),
        new HighPayoutCaseData(PARTNER_VENDOR, GOLD_VENDOR, 1000.0, 500.01, true, false),
        new HighPayoutCaseData(PARTNER_VENDOR, SILVER_VENDOR, 100_000.0, 500.01, true, true),
        new HighPayoutCaseData(PARTNER_VENDOR, SILVER_VENDOR, 100_000.0, 5000.0, true, true),
        new HighPayoutCaseData(GOLD_VENDOR, SILVER_VENDOR, 100_000.0, 0.0, true, true));
  }

  @ParameterizedTest
  @MethodSource("singleAssetCaseData")
  public void singleAsset(SingleAssetCaseData data) {
    searchResults.addFound(data.candidate);
    dealEligibility.put(data.candidate, data.eligible);

    sut.optimize(searchResults);

    thenAssetAdded(data.candidate, data.included);
  }

  @ParameterizedTest
  @MethodSource("highPayoutCaseData")
  public void highPayoutLowerGradeAsset(HighPayoutCaseData data) {
    searchResults.addFound(data.highPayout);
    dealEligibility.put(data.highPayout, true);

    searchResults.addFound(data.candidate);
    dealEligibility.put(data.candidate, data.eligible);

    sut.optimize(searchResults);

    thenHotspotHas(Deals, data.highPayout);
    thenAssetAdded(data.candidate, data.included);
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

  static class SingleAssetCaseData {
    private final Asset candidate;
    private final boolean eligible;
    private final boolean included;

    public SingleAssetCaseData(
        AssetVendor vendor, double revenue, double royalties, boolean eligible, boolean included) {
      candidate = asset(vendor, assetPurchaseInfo(revenue, royalties), assetPurchaseInfo());
      this.eligible = eligible;
      this.included = included;
    }
  }

  static class HighPayoutCaseData {
    private final Asset highPayout;
    private final Asset candidate;
    private final boolean eligible;
    private final boolean included;

    public HighPayoutCaseData(
        AssetVendor highPayoutVendor,
        AssetVendor vendor,
        double revenue,
        double royalties,
        boolean eligible,
        boolean included) {
      highPayout =
          asset(highPayoutVendor, assetPurchaseInfo(1_000_000.0, 0.0), assetPurchaseInfo());
      candidate = asset(vendor, assetPurchaseInfo(revenue, royalties), assetPurchaseInfo());
      this.eligible = eligible;
      this.included = included;
    }
  }
}
