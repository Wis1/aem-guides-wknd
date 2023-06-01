package com.adobe.aem.guides.wknd.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "AAA Osgi Config To Page Search Component",
        description = "Configuration for search component")
public @interface ServiceConfig {
    @AttributeDefinition(
            name = "Search Path",
            description = "Resource path for search query execution",
            type = AttributeType.STRING)
    public String getPathToSearch() default "/content/wknd";
}
