package com.sainsburys.imagefinder.model;

import java.util.List;

public class Collection {

    private List<Item> items;

    public Collection() {
    }

    public Collection(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}
