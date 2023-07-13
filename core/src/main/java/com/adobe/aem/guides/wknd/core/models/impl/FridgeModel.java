package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ItemInterface;
import com.adobe.aem.guides.wknd.core.models.Properties;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.math.BigDecimal;
import java.util.List;

@Model(
        adaptables = {Resource.class},
        adapters = {ItemInterface.class},
        resourceType = {SellOfferItemImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FridgeModel implements ItemInterface {

    @ValueMapValue(name = "basefridgeprice")
    private BigDecimal fridgePrice;

    @ValueMapValue(name = "fridgeproducer")
    private String fridgeProducer;

    @ValueMapValue(name = "fridgemodel")
    private String fridgeModel;

    @ChildResource(name = "fridgeproperties")
    public List<Properties> properties;

    public BigDecimal getPrice() {
        return fridgePrice;
    }

    public String getProducer() {
        return fridgeProducer;
    }

    public String getModel() {
        return fridgeModel;
    }

    public List<Properties> getProperties() {
        return properties;
    }
}
