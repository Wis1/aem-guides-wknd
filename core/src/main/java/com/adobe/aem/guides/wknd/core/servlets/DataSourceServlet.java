package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.models.ProviderService;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.json.JSONException;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.adobe.aem.guides.wknd.core.servlets.AppConstants.*;
import static com.adobe.aem.guides.wknd.core.servlets.DataSourceServlet.RESOURCE_TYPE;
import static com.adobe.aem.guides.wknd.core.servlets.DataSourceServlet.SERVICE_NAME;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_ID + EQUALS + SERVICE_NAME,
                SLING_SERVLET_RESOURCE_TYPES + EQUALS + RESOURCE_TYPE
        }
)
public class DataSourceServlet extends SlingSafeMethodsServlet {

    protected static final String SERVICE_NAME = "Dynamic DataSource Servlet";
    protected static final String RESOURCE_TYPE = "wknd/components/datasource";
    private static final long serialVersionUID = 4235730140092283425L;
    private static final String TAG = DataSourceServlet.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceServlet.class);

    @Reference
    private ProviderService providerService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        // Getting resource resolver from the current request
        ResourceResolver resourceResolver = request.getResourceResolver();
        // Get the resource object for the path from where the request is fired
        Resource currentResource = request.getResource();
        // Get the dropdown selector
        Stream<Map.Entry<String,String>> streamData = Optional.ofNullable(currentResource)
                .map(resource -> resource.getChild(DATASOURCE))
                .map(Resource::getValueMap)
                .map(valueMap -> valueMap.get(DROPDOWN_SELECTOR, String.class))
                .map(optional->getJsonResource(resourceResolver,optional))
                .map(resource -> {
                    try {
                        return providerService.read(resource);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(Stream.empty());
        createData(streamData, request, resourceResolver);
    }

    public void createData(Stream<Map.Entry<String, String>> streamData, SlingHttpServletRequest request, ResourceResolver resourceResolver) {
        // Creating the data source object
        @SuppressWarnings({"unchecked", "rawtypes"})
        DataSource ds = new SimpleDataSource(new TransformIterator<>(streamData.iterator(), entry -> {
            String dropValue = entry.getKey();
            ValueMap vm = new ValueMapDecorator(new HashMap<>());
            vm.put("text", dropValue);
            vm.put("value", entry.getValue());
            return new ValueMapResource(resourceResolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, vm);
        }));
        request.setAttribute(DataSource.class.getName(), ds);
    }

    private Resource getJsonResource(ResourceResolver resourceResolver, String dropdownSelector) {
        Resource jsonResource;
        switch (dropdownSelector) {
            case PRODUCER_LIST:
                jsonResource = resourceResolver.getResource(PRODUCER_LIST_PATH);
                break;
            case TVMODEL_LIST:
                jsonResource = resourceResolver.getResource(TVMODEL_LIST_PATH);
                break;
            case FRIDGEMODEL_LIST:
                jsonResource = resourceResolver.getResource(FRIDGEMODEL_LIST_PATH);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dropdownSelector);
        }
        return jsonResource;
    }
}
