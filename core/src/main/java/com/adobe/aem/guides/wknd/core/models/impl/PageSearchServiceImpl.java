package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.config.ServiceConfig;
import com.adobe.aem.guides.wknd.core.models.PageSearchService;
import com.adobe.aem.guides.wknd.core.models.SearchResultModel;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import javax.jcr.query.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component(service = PageSearchService.class)
@Designate(ocd = ServiceConfig.class)
public class PageSearchServiceImpl implements PageSearchService {
    private String path;

    private int numberOfResults;

    @Modified
    @Activate
    public void activate(ServiceConfig serviceConfig) {
        path = serviceConfig.getPathToSearch();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public List<SearchResultModel> getSearchResults(String searchPhrase,
                                                    int maxNumberOfSearchResults,
                                                    ResourceResolver resourceResolver) {
        String queryParam = "%" + searchPhrase + "%";
        String query = String.format(
                "SELECT * FROM [cq:Page] WHERE ISDESCENDANTNODE('%s') AND LOWER([jcr:content/jcr:title]) LIKE '%s'",
                getPath(), queryParam
        );

        List<SearchResultModel> searchResults = new ArrayList<>();
        resourceResolver.findResources(query, Query.JCR_SQL2)
                .forEachRemaining(resource -> {
                    searchResults.add(resource.adaptTo(SearchResultModel.class));
                });
        numberOfResults = searchResults.size();

        return searchResults.stream()
                .limit(maxNumberOfSearchResults)
                .collect(Collectors.toList());
    }

    @Override
    public int getNumberOfResults() {
        return numberOfResults;
    }
}
