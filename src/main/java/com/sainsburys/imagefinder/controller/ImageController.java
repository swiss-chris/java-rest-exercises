package com.sainsburys.imagefinder.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ImageController {

    @RequestMapping(name = "images", method = RequestMethod.GET)
    public List<String> get() {
        return
            ((Map<String, Map<String, List<Map<String, List<Map<String, String>>>>>>)
                new RestTemplate()
                    .getForObject("https://images-api.nasa.gov/search?q=clouds", Map.class)
            )
                .get("collection")
                .get("items").stream()
                .map(item -> item.get("links"))
                .flatMap(List::stream)
                .filter(link -> "preview".equals(link.get("rel")))
                .map(link -> link.get("href"))
                .collect(Collectors.toList());
    }
}
