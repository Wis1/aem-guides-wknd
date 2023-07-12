package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.Properties;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class SellOfferItemImplTest {

    private final AemContext ctx = new AemContext();
    private static final String SELLOFFERITEMWITHDISCOUNT="/content/sellofferitem-with-discount";
    private static final String SELLOFFERITEMWITHOUTDISCOUNT="/content/sellofferitem-without-discount";

    FridgeModel fridgeModel= mock(FridgeModel.class);

    TvModel tvModel= mock(TvModel.class);

    @InjectMocks
    SellOfferItemImpl impl;

    @BeforeEach
    public void setUp() {
        ctx.addModelsForClasses(SellOfferItemImpl.class);
        ctx.load().json("/com/adobe/aem/guides/wknd/core/models/impl/SellOfferItemTest.json",
                "/content");
    }

    private SellOfferItemImpl createSellOfferItemImplFromJson(String path) {
        return ctx.currentResource(path).adaptTo(SellOfferItemImpl.class);
    }

    @Test
    void shouldReturnCorrectResultWhenDiscountIsTurnOn() {
        // Given
        impl = createSellOfferItemImplFromJson(SELLOFFERITEMWITHDISCOUNT);
        when(fridgeModel.getPrice()).thenReturn(BigDecimal.valueOf(10000));

        //When
        Double actualPrice = impl.getTotalPrice(fridgeModel.getPrice());

        // Then
        assertEquals(8000, actualPrice);
    }


    @Test
    void shouldReturnCorrectResultWhenDiscountIsTurnOff() {
        // Given
        impl = createSellOfferItemImplFromJson(SELLOFFERITEMWITHOUTDISCOUNT);
        when(fridgeModel.getPrice()).thenReturn(BigDecimal.valueOf(10000));
        when(tvModel.getPrice()).thenReturn(BigDecimal.valueOf(1533.37));

        // When
        Double actualPriceFridge = impl.getTotalPrice(fridgeModel.getPrice());
        Double actualPriceTV = impl.getTotalPrice(tvModel.getPrice());

        // Then
        assertEquals(10000, actualPriceFridge);
        assertEquals(1533.37, actualPriceTV);
    }

    @Test
    void shouldReturnCorrectResultWhenDiscountIsTurnOnAndResultMustBeRounded() {
        // Given
        impl = createSellOfferItemImplFromJson(SELLOFFERITEMWITHDISCOUNT);
        when(tvModel.getPrice()).thenReturn(BigDecimal.valueOf(1533.37));

        // When
        Double actualPrice = impl.getTotalPrice(tvModel.getPrice());

        // Then
        assertEquals(1226.7, actualPrice);
    }

    @Test
    void shouldReturnCorrectResultWhenGivenPriceEqualsZero() {
        // Given
        impl = createSellOfferItemImplFromJson(SELLOFFERITEMWITHDISCOUNT);
        when(fridgeModel.getPrice()).thenReturn(BigDecimal.ZERO);
        // When
        Double actualPrice = impl.getTotalPrice(fridgeModel.getPrice());

        // Then
        assertEquals(0, actualPrice);
    }

    @Test
    void shouldReturnCorrectProduct() {
        // Given
        impl = createSellOfferItemImplFromJson(SELLOFFERITEMWITHDISCOUNT);

        // When
        String product = impl.getProduct();

        // Then
        assertEquals("tv", product);
    }

    @Test
    void shouldReturnCorrectValueOfDiscount() {
        // Given
        impl = createSellOfferItemImplFromJson(SELLOFFERITEMWITHDISCOUNT);

        // When
        Integer valueOfDiscount = impl.getDiscount();

        // Then
        assertEquals(20, valueOfDiscount);
    }

    @Test
    void shouldReturnTrueWhenSpecialOfferEqualsYes() {
        // Given
        impl = createSellOfferItemImplFromJson(SELLOFFERITEMWITHDISCOUNT);

        // When & Then
        assertTrue(impl.isSpecialOffer());
    }

    @Test
    void shouldReturnFalseWhenSpecialOfferEqualsNo() {
        // Given
        impl = createSellOfferItemImplFromJson(SELLOFFERITEMWITHOUTDISCOUNT);

        // When & Then
        assertFalse(impl.isSpecialOffer());
    }

    @Test
    void shouldReturnCorrectPathOfImage() {
        // Given
        impl = createSellOfferItemImplFromJson(SELLOFFERITEMWITHOUTDISCOUNT);

        String productImageRef = impl.getImage();
        // When & Then
        assertEquals("/content/dam/wknd/en/magazine/la-skateparks/tv.jpg", productImageRef);
    }

    @Test
    void shouldReturnCorrectPropertiesForCurrentModel() {
        // Given
        Properties properties1 = mock(Properties.class);
        when(properties1.getPropertyName()).thenReturn("Property 1");
        when(properties1.getPropertyValue()).thenReturn("Value 1");
        Properties properties2 = mock(Properties.class);
        when(properties2.getPropertyName()).thenReturn("Property 2");
        when(properties2.getPropertyValue()).thenReturn("Value 2");

        when(tvModel.getProperties()).thenReturn(Arrays.asList(properties1, properties2));

        // When
        impl.init();
        List<Properties> actualProperties = impl.getProperties();

        // Then
        assertEquals(2, actualProperties.size());
        assertEquals("Property 1", actualProperties.get(0).getPropertyName());
        assertEquals("Value 1", actualProperties.get(0).getPropertyValue());
        assertEquals("Property 2", actualProperties.get(1).getPropertyName());
        assertEquals("Value 2", actualProperties.get(1).getPropertyValue());
    }
}
