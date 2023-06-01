package com.adobe.aem.guides.wknd.core.models;

import java.util.List;

public interface PageSearchComponent {
    Integer getMaxNumberOfSearchResults();
    List<SearchResultModel> getSearchResults();
    String getMessageAboutSearchPhrase();
    boolean hasResults();
}
