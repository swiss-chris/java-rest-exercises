package com.sainsburys.imagefinder.controller;

import com.sainsburys.imagefinder.model.CollectionWrapper;
import com.sainsburys.imagefinder.model.Item;
import com.sainsburys.imagefinder.model.Link;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ImageController {

    @RequestMapping(value = "/images", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<String> getImages(@Nullable @RequestParam String q) {
        return new RestTemplate().getForObject("https://images-api.nasa.gov/search?q=" + q, CollectionWrapper.class)
            .getCollection()
            .getItems().stream()
            .map(Item::getLinks)
            .flatMap(List::stream)
            .filter(link -> "preview".equals(link.getRel()))
            .map(Link::getHref)
            .collect(Collectors.toList());
    }
}
