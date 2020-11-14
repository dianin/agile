package ru.home.agile.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

@Component
public class TokenWorker {

    private static final String URL = "http://interview.agileengine.com/auth";
    private static final String APIKEY = "23567b218376f79d9415";

    @Scheduled(cron = "*/50 * * * * *")
    @SneakyThrows
    public String getToken () {
        HashMap values = new HashMap<String, String>() {{
            put("apiKey", APIKEY);
        }};
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("content-type", "application/json")
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject myObject = new JSONObject(response.body());
        return myObject.getString("token");
    }

}
