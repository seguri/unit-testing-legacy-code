package com.assetco.hotspots.optimization;

import com.assetco.search.results.AssetTopic;

public interface AssetTopicsSource {
  Iterable<AssetTopic> getTopics();
}
