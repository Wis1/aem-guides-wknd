package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class)
public interface Properties {
    @ValueMapValue(name = "propertyname")
    String getPropertyName();

    @ValueMapValue(name = "propertyvalue")
    String getPropertyValue();
}
