package com.moengage.sample.java;

import org.jetbrains.annotations.NotNull;

/**
 * @author Arshiya Khanum
 */
public final class HomeCategory {

  @NotNull private final FeaturesCategory category;

  @NotNull private final String label;

  public HomeCategory(@NotNull FeaturesCategory category, @NotNull String label) {
    this.category = category;
    this.label = label;
  }

  public FeaturesCategory getCategory() {
    return category;
  }

  public String getLabel() {
    return label;
  }
}
