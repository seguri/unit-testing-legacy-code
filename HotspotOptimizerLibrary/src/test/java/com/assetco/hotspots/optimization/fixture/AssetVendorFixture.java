package com.assetco.hotspots.optimization.fixture;

import com.assetco.search.results.AssetVendor;
import com.assetco.search.results.AssetVendorRelationshipLevel;
import java.util.UUID;

public class AssetVendorFixture {

  public static AssetVendor assetVendor(AssetVendorRelationshipLevel level) {
    return new AssetVendor(UUID.randomUUID().toString(), "DISPLAY_NAME", level, 0);
  }
}
