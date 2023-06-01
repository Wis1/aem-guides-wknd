package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.config.ServiceConfig;
import com.adobe.aem.guides.wknd.core.models.SearchResultModel;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class PageSearchComponentImplTest {

    private final AemContext ctx = new AemContext(ResourceResolverType.JCR_OAK);
    PageSearchServiceImpl pageSearchServiceImp;

    @BeforeEach
    public void setUp() {
        pageSearchServiceImp= ctx.registerService(new PageSearchServiceImpl());
        ServiceConfig config= mock(ServiceConfig.class);
        when(config.getPathToSearch()).thenReturn("/content/wknd");
        pageSearchServiceImp.activate(config);

        ctx.load().json(
                "/com/adobe/aem/guides/wknd/core/models/impl/PageSearchComponentTest.json",
                "/content/wknd");
    }

    @Test
    void testGetSearchResultsWhenNameIsCorrectForAllPage() {
        // Given
        ResourceResolver resourceResolver = ctx.resourceResolver();
        int maxNumberOfSearchResults = 5;
        String searchPhrase="item";

        // When
        List<SearchResultModel> searchResults =
                pageSearchServiceImp.getSearchResults(searchPhrase, maxNumberOfSearchResults, resourceResolver);

        // Then
        assertEquals(4, searchResults.size());
    }

    @Test
    void testGetSearchResultsWhenNameIsWrongForAllPage() {
        // Given
        ResourceResolver resourceResolver = ctx.resourceResolver();
        int maxNumberOfSearchResults = 5;
        String searchPhrase="page";

        // When
        List<SearchResultModel> searchResults =
                pageSearchServiceImp.getSearchResults(searchPhrase, maxNumberOfSearchResults, resourceResolver);

        // Then
        assertEquals(0, searchResults.size());
    }

    @Test
    void testGetSearchResultsWhenNameIsCorrectForOnePage() {
        // Given
        ResourceResolver resourceResolver = ctx.resourceResolver();
        int maxNumberOfSearchResults = 5;
        String searchPhrase="1";

        // When
        List<SearchResultModel> searchResults =
                pageSearchServiceImp.getSearchResults(searchPhrase, maxNumberOfSearchResults, resourceResolver);

        // Then
        assertEquals(1, searchResults.size());
        assertEquals("Ultimate Guide to LA Skateparks", searchResults.get(0).getTitle());
    }
}
