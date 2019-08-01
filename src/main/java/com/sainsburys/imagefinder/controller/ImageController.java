package com.sainsburys.imagefinder.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ImageController {

    @RequestMapping(value = "/images", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<String> getImages() {
        List<String> hrefs = new ArrayList<>();
        String json = new RestTemplate().getForObject("https://images-api.nasa.gov/search?q=clouds", String.class);
        new JsonParser().parse(json).getAsJsonObject()
                .get("collection").getAsJsonObject()
                .get("items").getAsJsonArray()
                .forEach(item -> item.getAsJsonObject()
                        .get("links").getAsJsonArray()
                        .forEach(link -> {
                            JsonObject linkObject = link.getAsJsonObject();
                            String relString = linkObject.get("rel").getAsString();
                            if ("preview".equals(relString)) {
                                hrefs.add(linkObject.get("href").getAsString());
                            }
                        })
                );
        return hrefs;
    }
}
