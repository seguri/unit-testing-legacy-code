package com.assetco.hotspots.optimization;

import static com.assetco.hotspots.optimization.fixture.AssetFixture.asset;
import static com.assetco.hotspots.optimization.fixture.AssetVendorFixture.assetVendor;
import static com.assetco.search.results.HotspotKey.Showcase;

import com.assetco.search.results.Asset;
import com.assetco.search.results.HotspotKey;
import java.util.List;
import org.junit.jupiter.api.Test;

class BugsTest {

  @Test
  void precedingPartnerWithLongTrailingAssetsDoesNotWin() {
    var partnerVendor = assetVendor();
    var otherPartnerVendor = assetVendor();
    var missing = asset(partnerVendor);
    var otherMissing = asset(otherPartnerVendor);
    var expected =
        List.of(
            asset(partnerVendor), asset(partnerVendor), asset(partnerVendor), asset(partnerVendor));

    whenOptimize();

    thenHotspotDoesNotHave(Showcase, missing);
    thenHotspotHasExactly(Showcase, expected);
  }

  private void whenOptimize() {}

  private void thenHotspotDoesNotHave(HotspotKey key, Asset... assets) {}

  private void thenHotspotHasExactly(HotspotKey key, List<Asset> assets) {}
}
