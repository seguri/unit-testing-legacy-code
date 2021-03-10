package com.assetco.search.results;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AssetTopicTests {
  @Test
  public void storesAndRetrievesValues() {
    var id = Any.string();
    var displayName = Any.string();

    var topic = new AssetTopic(id, displayName);

    assertEquals(id, topic.getId());
    assertEquals(displayName, topic.getDisplayName());
  }
}
