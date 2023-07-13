package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.models.ProviderService;
import org.apache.sling.api.resource.Resource;
import org.json.JSONArray;
import org.json.JSONException;
import org.osgi.service.component.annotations.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

@Component(service = ProviderService.class)
public class DataSourceProviderServlet implements ProviderService {

    @Override
    public Stream<Map.Entry<String, String>> read(Resource jsonResource) throws JSONException, IOException {
        StringBuilder jsonContent = new StringBuilder();

        Optional<InputStream> inputStreamOptional = Optional.ofNullable(jsonResource.adaptTo(InputStream.class));
        BufferedReader jsonReader = new BufferedReader(
                new InputStreamReader(inputStreamOptional.orElseThrow(() ->
                        new RuntimeException("JSON resource is missing or cannot be adapted to InputStream.")),
                        StandardCharsets.UTF_8));

        // Loop through each line
        String line;
        while ((line = jsonReader.readLine()) != null) {
            jsonContent.append(line);
        }
        JSONArray jsonArray = new JSONArray(jsonContent.toString());
        Map<String, String> data = new TreeMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            data.put(jsonArray.getJSONObject(i).getString("text"),
                    jsonArray.getJSONObject(i).getString("value"));
        }
        return data.entrySet().stream();
    }
}
