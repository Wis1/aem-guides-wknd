package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchResultModel {
    @Self
    private Resource resource;

    @ValueMapValue(name = "jcr:content/jcr:title")
    private String title;

    private String path;

    @ValueMapValue(name = "jcr:content/cq:featuredimage/fileReference")
    private String pageThumbnail;

    @PostConstruct
    private void init() {
        path = resource.getPath();
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getPageThumbnail() {
        return pageThumbnail;
    }
}
