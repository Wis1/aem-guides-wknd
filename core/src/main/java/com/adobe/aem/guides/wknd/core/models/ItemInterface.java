package com.adobe.aem.guides.wknd.core.models;

import java.math.BigDecimal;
import java.util.List;

public interface ItemInterface {

    BigDecimal getPrice();

    String getProducer();

    String getModel();

    List<Properties> getProperties();
}
