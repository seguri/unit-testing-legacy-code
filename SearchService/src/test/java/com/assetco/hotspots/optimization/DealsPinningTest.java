package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetPurchaseInfoFixture.assetPurchaseInfo;
import static com.assetco.search.results.HotspotKey.Deals;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetVendor;
import org.junit.jupiter.api.Test;

class DealsPinningTest extends AbstractOptimizerTest {

  @Test
  public void singleAsset() {
    // parameterized initial case
    singleAssetCase(PARTNER_VENDOR, "0", "1000", true, true);
    // easy to generate many, many new cases in a very small amount of time
    singleAssetCase(PARTNER_VENDOR, "0", "1000", false, true);
    singleAssetCase(PARTNER_VENDOR, "200", "1000", true, true);
    singleAssetCase(PARTNER_VENDOR, "600", "1000", true, true);
    // run, fix expectation, repeat
    singleAssetCase(PARTNER_VENDOR, "999.99", "1000", true, false);
    singleAssetCase(PARTNER_VENDOR, "700", "1000", true, true);
    singleAssetCase(PARTNER_VENDOR, "700.001", "1000", true, false);
    singleAssetCase(PARTNER_VENDOR, "7", "10", true, true);
    singleAssetCase(GOLD_VENDOR, "0", "1000", true, true);
    singleAssetCase(GOLD_VENDOR, "0", "1000", false, true);
    singleAssetCase(GOLD_VENDOR, "200", "1000", true, true);
    singleAssetCase(GOLD_VENDOR, "600", "1000", true, true);
    singleAssetCase(GOLD_VENDOR, "999.99", "1000", true, false);
    singleAssetCase(GOLD_VENDOR, "700", "1000", true, true);
    singleAssetCase(GOLD_VENDOR, "700", "1000", false, false);
    singleAssetCase(GOLD_VENDOR, "700.001", "1000", true, false);
    singleAssetCase(GOLD_VENDOR, "7", "10", true, false);
    singleAssetCase(SILVER_VENDOR, "0", "1000", true, false);
    singleAssetCase(SILVER_VENDOR, "0", "1000", false, false);
    singleAssetCase(SILVER_VENDOR, "200", "1000", true, false);
    singleAssetCase(SILVER_VENDOR, "600", "1000", true, false);
    singleAssetCase(SILVER_VENDOR, "999.99", "1000", true, false);
    singleAssetCase(SILVER_VENDOR, "700", "1000", true, false);
    singleAssetCase(SILVER_VENDOR, "700.001", "1000", true, false);
    singleAssetCase(SILVER_VENDOR, "0", "1500", true, true);
    singleAssetCase(SILVER_VENDOR, "0", "1500", false, true);
    singleAssetCase(SILVER_VENDOR, "200", "1500", true, true);
    singleAssetCase(SILVER_VENDOR, "600", "1500", true, true);
    singleAssetCase(SILVER_VENDOR, "600", "1500", false, true);
    singleAssetCase(SILVER_VENDOR, "999.99", "1500", true, true);
    singleAssetCase(SILVER_VENDOR, "999.99", "1500", false, false);
    singleAssetCase(SILVER_VENDOR, "700", "1500", true, true);
    singleAssetCase(SILVER_VENDOR, "700.001", "1500", true, true);
    singleAssetCase(SILVER_VENDOR, "750.001", "1500", true, true);
    singleAssetCase(SILVER_VENDOR, "1500.001", "2000", true, false);
    singleAssetCase(SILVER_VENDOR, "7", "10", true, false);
    singleAssetCase(BASIC_VENDOR, "0", "1000", true, false);
    singleAssetCase(BASIC_VENDOR, "200", "1000", true, false);
    singleAssetCase(BASIC_VENDOR, "600", "1000", true, false);
    singleAssetCase(BASIC_VENDOR, "999.99", "1000", true, false);
    singleAssetCase(BASIC_VENDOR, "700", "1000", true, false);
    singleAssetCase(BASIC_VENDOR, "700.001", "1000", true, false);
    singleAssetCase(BASIC_VENDOR, "7", "10", true, false);
  }

  // noticed, looking at the code, that having multiple levels of vendor affects outcome
  @Test
  public void highPayoutLowerGradeAsset() {
    highPayoutLowerGradeAssetCases(PARTNER_VENDOR, GOLD_VENDOR, "0", "1000", true, true);
    highPayoutLowerGradeAssetCases(PARTNER_VENDOR, GOLD_VENDOR, "0", "1000", false, false);
    highPayoutLowerGradeAssetCases(PARTNER_VENDOR, GOLD_VENDOR, "700", "1000", true, false);
    highPayoutLowerGradeAssetCases(PARTNER_VENDOR, GOLD_VENDOR, "500", "1000", true, true);
    highPayoutLowerGradeAssetCases(PARTNER_VENDOR, GOLD_VENDOR, "500.01", "1000", true, false);
    highPayoutLowerGradeAssetCases(PARTNER_VENDOR, SILVER_VENDOR, "0", "100000", true, true);
    highPayoutLowerGradeAssetCases(PARTNER_VENDOR, SILVER_VENDOR, "5000", "100000", true, true);
    highPayoutLowerGradeAssetCases(PARTNER_VENDOR, SILVER_VENDOR, "5000", "100000", true, true);
    highPayoutLowerGradeAssetCases(GOLD_VENDOR, SILVER_VENDOR, "0", "100000", true, true);
    highPayoutLowerGradeAssetCases(GOLD_VENDOR, SILVER_VENDOR, "0", "100000", false, false);
    highPayoutLowerGradeAssetCases(GOLD_VENDOR, SILVER_VENDOR, "0", "2000", true, true);
    highPayoutLowerGradeAssetCases(GOLD_VENDOR, SILVER_VENDOR, "0", "1500", true, true);
    highPayoutLowerGradeAssetCases(GOLD_VENDOR, SILVER_VENDOR, "0", "1499.99", true, false);
    highPayoutLowerGradeAssetCases(GOLD_VENDOR, SILVER_VENDOR, "500", "1499.99", true, false);
    highPayoutLowerGradeAssetCases(GOLD_VENDOR, SILVER_VENDOR, "750", "1500", true, true);
    highPayoutLowerGradeAssetCases(GOLD_VENDOR, SILVER_VENDOR, "750.001", "1500", true, false);
  }

  @Test
  public void lowPayoutLowerGradeAssetCases() {}

  private void singleAssetCase(
      AssetVendor vendor,
      String royalties,
      String revenue,
      boolean isDealEligible,
      boolean shouldBeAdded) {
    setup();

    var candidate = asset(vendor, assetPurchaseInfo(revenue, royalties), assetPurchaseInfo());
    searchResults.addFound(candidate);
    dealEligibility.put(candidate, isDealEligible);

    sut.optimize(searchResults);

    thenAssetAdded(candidate, shouldBeAdded);
  }

  private void highPayoutLowerGradeAssetCases(
      AssetVendor highPayoutVendor,
      AssetVendor vendor,
      String royalties,
      String revenue,
      boolean isDealEligible,
      boolean shouldBeAdded) {
    setup();

    var highPayout =
        asset(highPayoutVendor, assetPurchaseInfo("1000000.00", "0"), assetPurchaseInfo());
    searchResults.addFound(highPayout);
    dealEligibility.put(highPayout, true);

    var candidate = asset(vendor, assetPurchaseInfo(revenue, royalties), assetPurchaseInfo());
    searchResults.addFound(candidate);
    dealEligibility.put(candidate, isDealEligible);

    sut.optimize(searchResults);

    thenHotspotHas(Deals, highPayout);
    thenAssetAdded(candidate, shouldBeAdded);
  }

  private void thenAssetAdded(Asset candidate, boolean shouldBeAdded) {
    if (shouldBeAdded) {
      thenHotspotHas(Deals, candidate);
    } else {
      thenHotspotHasNot(Deals, candidate);
    }
  }
}
