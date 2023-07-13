package com.adobe.aem.guides.wknd.core.models.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SellOfferItemImplUnitTest {
    SellOfferItemImpl impl;

    @BeforeEach
    public void setUp() {
        impl = new SellOfferItemImpl();
    }

    @Test
    void shouldReturnCorrectTotalPriceWhenDiscountIsTurnOn() {
        // Given
        BigDecimal price = BigDecimal.valueOf(10000);
        impl.setDiscount(20);
        impl.setSpecialoffer("Yes");
        // When
        Double actualPrice = impl.getTotalPrice(price);
        // Then
        assertEquals(8000, actualPrice);
    }

    @Test
    void shouldReturnCorrectTotalPriceWhenDiscountIsTurnOff() {
        // Given
        BigDecimal price = BigDecimal.valueOf(10000);
        impl.setDiscount(20);
        impl.setSpecialoffer("No");
        // When
        Double actualPrice = impl.getTotalPrice(price);
        //Then
        assertEquals(10000, actualPrice);
    }

    @Test
    void shouldReturnCorrectTotalPriceWhenDiscountIsTurnOnAndResultMustBeRounded() {
        // Given
        BigDecimal price = BigDecimal.valueOf(247.54);
        impl.setDiscount(13);
        impl.setSpecialoffer("Yes");
        // When
        Double actualPrice = impl.getTotalPrice(price);
        //Then
        assertEquals(215.36, actualPrice);
    }

    @Test
    void shouldReturnZeroWhenGivenPriceEqualsZero() {
        // Given
        BigDecimal price = BigDecimal.ZERO;
        impl.setDiscount(13);
        impl.setSpecialoffer("Yes");
        // When
        Double actualPrice = impl.getTotalPrice(price);
        //Then
        assertEquals(0.0, actualPrice);
    }
}
