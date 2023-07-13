package com.adobe.aem.guides.wknd.core.models;

import java.math.BigDecimal;
import java.util.List;

public interface SellOfferItem {

    String getImage();

    List<Properties> getProperties();

    Double getPrice();

    boolean isSpecialOffer();

    Double getTotalPrice(BigDecimal price);

}
