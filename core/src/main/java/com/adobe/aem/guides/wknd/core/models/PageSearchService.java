package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface PageSearchService {
    String getPath();

    List<SearchResultModel> getSearchResults(String searchPhrase,
                                             int maxNumberOfSearchResults,
                                             ResourceResolver resourceResolver);

    int getNumberOfResults();
}
