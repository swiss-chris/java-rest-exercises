package com.sainsburys.imagefinder.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ImageController {

    @RequestMapping(value = "/images", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<String> getImages() {
        List<String> hrefs = new ArrayList<>();
        String uri = "https://images-api.nasa.gov/search?q=clouds";
        try {
            URLConnection request = new URL(uri).openConnection();
            request.connect();
            InputStreamReader inputStreamReader = new InputStreamReader((InputStream) request.getContent());
            JsonElement root = new JsonParser().parse(inputStreamReader);
            JsonArray items = root
                    .getAsJsonObject()
                    .get("collection").getAsJsonObject()
                    .get("items").getAsJsonArray();
            JsonArray links = new JsonArray();
            items.forEach(item -> links.addAll(item
                    .getAsJsonObject()
                    .get("links")
                    .getAsJsonArray()));
            links.forEach(link -> {
                JsonObject linkObject = link.getAsJsonObject();
                String relString = linkObject.get("rel").getAsString();
                if ("preview".equals(relString)) {
                    hrefs.add(linkObject.get("href").getAsString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hrefs;
    }
}
