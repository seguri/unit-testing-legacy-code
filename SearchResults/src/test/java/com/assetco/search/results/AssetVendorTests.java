package com.assetco.search.results;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AssetVendorTests {
  @Test
  public void createAndGetValues() {
    var id = Any.string();
    var displayName = Any.string();
    var relationshipLevel = Any.relationshipLevel();
    long royaltyRate = Any.anyLong();

    var vendor = new AssetVendor(id, displayName, relationshipLevel, royaltyRate);

    assertEquals(id, vendor.getId());
    assertEquals(displayName, vendor.getDisplayName());
    assertEquals(relationshipLevel, vendor.getRelationshipLevel());
    assertEquals(royaltyRate, vendor.getRoyaltyRate(), .5f);
  }
}
