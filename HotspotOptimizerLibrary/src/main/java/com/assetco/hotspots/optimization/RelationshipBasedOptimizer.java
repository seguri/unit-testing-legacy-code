package com.assetco.hotspots.optimization;

import static com.assetco.search.results.AssetVendorRelationshipLevel.Gold;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Partner;
import static com.assetco.search.results.AssetVendorRelationshipLevel.Silver;
import static com.assetco.search.results.HotspotKey.Fold;
import static com.assetco.search.results.HotspotKey.HighValue;
import static com.assetco.search.results.HotspotKey.Showcase;
import static com.assetco.search.results.HotspotKey.TopPicks;

import com.assetco.search.results.Asset;
import com.assetco.search.results.AssetVendor;
import com.assetco.search.results.Hotspot;
import com.assetco.search.results.SearchResults;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

class RelationshipBasedOptimizer {
  public void optimize(SearchResults searchResults) {
    var showcaseAssets = new ArrayList<Asset>();
    var showcaseAssetsByPartner = new HashMap<AssetVendor, ArrayList<Asset>>();
    var partnerAssets = new ArrayList<Asset>();
    var goldAssets = new ArrayList<Asset>();
    var silverAssets = new ArrayList<Asset>();

    for (Asset asset : searchResults.getFound()) {
      var vendor = asset.getVendor();
      if (vendor.getRelationshipLevel() == Gold) {
        goldAssets.add(asset);
      } else if (vendor.getRelationshipLevel() == Silver) {
        silverAssets.add(asset);
      }

      if (vendor.getRelationshipLevel() != Partner) {
        continue;
      }

      partnerAssets.add(asset);

      if (showcaseAssets.isEmpty()) {
        // Get or initialize assets for the current vendor
        showcaseAssets =
            showcaseAssetsByPartner.merge(vendor, new ArrayList<>(), (old, new_) -> old);
      }

      if (showcaseAssets.size() >= 5) {
        if (Objects.equals(showcaseAssets.get(0).getVendor(), vendor)) {
          searchResults.getHotspot(TopPicks).addMember(asset);
        }
      } else {
        if (showcaseAssets.size() != 0) {
          if (!Objects.equals(showcaseAssets.get(0).getVendor(), vendor)) {
            if (showcaseAssets.size() < 3) {
              // Don't delete the assets, get or initialize assets for the current vendor instead
              showcaseAssets =
                  showcaseAssetsByPartner.merge(vendor, new ArrayList<>(), (old, new_) -> old);
            }
          }
        }

        if (showcaseAssets.size() == 0
            || Objects.equals(showcaseAssets.get(0).getVendor(), vendor)) {
          showcaseAssets.add(asset);
        }
      }
    }

    var highValueHotspot = searchResults.getHotspot(HighValue);
    for (var asset : partnerAssets) {
      if (!highValueHotspot.getMembers().contains(asset)) {
        highValueHotspot.addMember(asset);
      }
    }

    for (var asset : partnerAssets) {
      searchResults.getHotspot(Fold).addMember(asset);
    }

    var showcaseEmpty = searchResults.getHotspot(Showcase).getMembers().isEmpty();
    if (showcaseEmpty && showcaseAssets.size() >= 3) {
      Hotspot showcaseHotspot = searchResults.getHotspot(Showcase);
      for (Asset asset : showcaseAssets) {
        showcaseHotspot.addMember(asset);
      }
    }

    for (var asset : goldAssets) {
      if (!highValueHotspot.getMembers().contains(asset)) {
        highValueHotspot.addMember(asset);
      }
    }

    for (var asset : goldAssets) {
      searchResults.getHotspot(Fold).addMember(asset);
    }

    for (var asset : silverAssets) {
      searchResults.getHotspot(Fold).addMember(asset);
    }
  }
}
