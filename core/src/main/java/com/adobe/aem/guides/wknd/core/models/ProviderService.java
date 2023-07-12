package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public interface ProviderService {

    Stream<Map.Entry<String, String>> read(Resource jsonResource) throws IOException, JSONException;
}
