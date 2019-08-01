package com.sainsburys.imagefinder.model;

import java.util.List;

public class Item {

    private List<Link> links;

    public Item() {
    }

    public Item(List<Link> links) {
        this.links = links;
    }

    public List<Link> getLinks() {
        return links;
    }
}
