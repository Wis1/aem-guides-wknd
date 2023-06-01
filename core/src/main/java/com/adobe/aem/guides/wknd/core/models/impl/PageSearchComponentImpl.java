package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.PageSearchComponent;
import com.adobe.aem.guides.wknd.core.models.PageSearchService;
import com.adobe.aem.guides.wknd.core.models.SearchResultModel;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {PageSearchComponent.class},
        resourceType = {PageSearchComponentImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PageSearchComponentImpl implements PageSearchComponent {

    protected static final String RESOURCE_TYPE = "wknd/components/pagesearchcomponent";

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private PageSearchService pageSearchService;

    @ValueMapValue
    private int maxNumberOfSearchResults;

    private List<SearchResultModel> searchResultModels;

    @Override
    public Integer getMaxNumberOfSearchResults() {
        return this.maxNumberOfSearchResults;
    }

    @PostConstruct
    public void init() {
        String searchPhrase = getSearchPhrase();
        ResourceResolver resourceResolver = request.getResourceResolver();
        if (isNotNullAndNotEmptyParameter()) {
            searchResultModels = initializeSearchResults(searchPhrase,
                    maxNumberOfSearchResults, resourceResolver);
        }
    }

    public boolean isNotNullAndNotEmptyParameter() {
        return StringUtils.isNotEmpty(getSearchPhrase());
    }

    public String getSearchPhrase() {
        return Optional.ofNullable(request.getRequestParameter("searchform")).map(Object::toString).orElse("");
    }

    public List<SearchResultModel> initializeSearchResults(String searchPhrase,
                                                           int maxNumberOfSearchResults,
                                                           ResourceResolver resourceResolver) {
        return pageSearchService.getSearchResults(searchPhrase, maxNumberOfSearchResults, resourceResolver);
    }

    public String getMessageAboutSearchPhrase() {
        String message = String.format("Shows results for: \"%s\"", getSearchPhrase());

        if (!isNotNullAndNotEmptyParameter()) {
            return "Nothing to search";
        }
        if (searchResultModels.isEmpty()) {
            message += ".\r No results found";
        }
        return message;
    }

    @Override
    public boolean hasResults() {
        return !searchResultModels.isEmpty();
    }

    @Override
    public List<SearchResultModel> getSearchResults() {
        return searchResultModels;
    }

    public int getNumberOfResults() {
        return pageSearchService.getNumberOfResults();
    }

    public int displayedResults() {
        return Math.min(getNumberOfResults(), getMaxNumberOfSearchResults());
    }
}
