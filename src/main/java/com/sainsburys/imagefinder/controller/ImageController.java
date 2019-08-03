package com.sainsburys.imagefinder.controller;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ImageController {

    @CrossOrigin("http://localhost:3000")
    @RequestMapping(name = "images", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> get(@Nullable @RequestParam String q) {
        if (q == null || "".equals(q)) {
            q = "clouds";
        }
        return
            ((Map<String, Map<String, List<Map<String, List<Map<String, String>>>>>>)
                new RestTemplate()
                    .getForObject("https://images-api.nasa.gov/search?q=" + q, Map.class)
            ).get("collection")
                .get("items")
                .stream()
                .filter(item -> item.containsKey("links"))
                .map(item -> item.get("links"))
                .flatMap(links -> links.stream())
                .filter(link -> "preview".equals(link.get("rel")))
                .map(link -> link.get("href"))
                .collect(Collectors.toList());
    }

}
