package com.binhtv.productservice.model.enums;

import java.util.Arrays;

public enum ProductSortField {
    ID("id"),
    CREATED_AT("createdAt"),
    PRICE("price"),
    SOLD_COUNT("soldCount"),
    AVERAGE_RATING("averageRating");

    private final String property;

    ProductSortField(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public static ProductSortField from(String value) {
        return Arrays.stream(values())
                .filter(field -> field.property.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported product sort field: %s.".formatted(value)));
    }
}
