package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ItemInterface;
import com.adobe.aem.guides.wknd.core.models.Properties;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.math.BigDecimal;
import java.util.*;

@Model(
        adaptables = {Resource.class},
        adapters = {ItemInterface.class},
        resourceType = {SellOfferItemImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TvModel implements ItemInterface {

    @ChildResource(name = "tvproperties")
    public List<com.adobe.aem.guides.wknd.core.models.Properties> properties;

    @ValueMapValue(name = "basetvprice")
    private BigDecimal tvPrice;

    @ValueMapValue(name = "tvproducer")
    private String tvProducer;
    @ValueMapValue(name = "tvmodel")
    private String tvModel;

    public List<Properties> getProperties() {
        return properties;
    }

    public BigDecimal getPrice() {
        return tvPrice;
    }

    public String getProducer() {
        return tvProducer;
    }

    public String getModel() {
        return tvModel;
    }
}
