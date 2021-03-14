package com.assetco.hotspots.optimization.fixture;

import static java.util.Collections.emptyList;

import com.assetco.search.results.AssetTopic;
import java.util.List;
import java.util.UUID;

public class AssetTopicFixture {

  public static AssetTopic assetTopic() {
    return new AssetTopic(UUID.randomUUID().toString(), "DISPLAY_NAME");
  }

  public static List<AssetTopic> assetTopics() {
    return List.of(assetTopic(), assetTopic(), assetTopic());
  }

  public static List<AssetTopic> emptyAssetTopics() {
    return emptyList();
  }
}
