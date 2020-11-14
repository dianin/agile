package ru.home.agile.Service;

import lombok.SneakyThrows;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.home.agile.entities.Image;
import ru.home.agile.repository.ImageRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@ComponentScan("ru.home.agile.Service")
@ComponentScan("ru.home.agile.repository")
@Component
public class ImageService {

    @Autowired
    private TokenWorker tokenWorker;

    @Autowired
    private ImageRepository imageRepository;


    private static final String URL = "http://interview.agileengine.com/images";
    private static final String urlWithPages = "http://interview.agileengine.com/images?page=%s";
    private static final String idUrl = "http://interview.agileengine.com/images/%s";

    @EventListener(ApplicationReadyEvent.class)
    public void saveAllImages() {
        int pages = getPageCount();
        getImagesId(pages);
        List<Image> images = imageRepository.findAll();
        images.forEach(this::updateImageEntity);
    }


    @SneakyThrows
    public Integer getPageCount() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("content-type", "application/json")
                .setHeader("Authorization", "Bearer " + tokenWorker.getToken())
                .uri(URI.create(URL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject myObject = new JSONObject(response.body());
        System.out.println(Integer.parseInt(myObject.getString("pageCount")));
        return Integer.parseInt(myObject.getString("pageCount"));
    }


    @SneakyThrows
    public void getImagesId(int pages) {
        for (int i = 0; i <= pages; i++) {
            String imagesEndPoint = String.format(urlWithPages, i);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("content-type", "application/json")
                    .setHeader("Authorization", "Bearer " + tokenWorker.getToken())
                    .uri(URI.create(imagesEndPoint))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            JSONObject myObject = new JSONObject(response.body());
            JSONArray list = myObject.getJSONArray("pictures");

            List<Image> images = new ArrayList<>();
            for (int j = 0; j < list.length() - 1; j++) {
                Image image = new Image();
                image.setSiteId(list.getJSONObject(j).get("id").toString());
                images.add(image);
            }
            imageRepository.saveAll(images);
        }
    }

    @SneakyThrows
    public void updateImageEntity(Image image) {
        String siteId = image.getSiteId();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("content-type", "application/json")
                .setHeader("Authorization", "Bearer " + tokenWorker.getToken())
                .uri(URI.create(String.format(idUrl, siteId)))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        JSONObject myObject = new JSONObject(response.body());

        try {
            image.setAuthor(myObject.get("author").toString());
            image.setCamera(myObject.get("camera").toString());
            image.setTags(myObject.get("tags").toString());
            image.setCroppedPicture(myObject.get("cropped_picture").toString());
            image.setFullPicture(myObject.get("full_picture").toString());
        } catch (JSONException e) {
            //TODO add entities with empty fields
            System.out.println("one entity with id:" + image.getId() + "not added");
        }
        imageRepository.save(image);
    }


}
