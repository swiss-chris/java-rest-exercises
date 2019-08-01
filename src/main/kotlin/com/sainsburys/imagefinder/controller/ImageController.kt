package com.sainsburys.imagefinder.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

data class CollectionWrapper(val collection: Collection)
data class Collection(val items: List<Item>)
data class Item(val links: List<Link>)
data class Link(val rel: String, val href: String)

@RestController
class ImageController {

    val images: List<String>
        @RequestMapping(value = ["/images"], method = [RequestMethod.GET], headers = ["Accept=application/json"])
        get() {
            val collectionWrapper = RestTemplate().getForObject("https://images-api.nasa.gov/search?q=clouds", CollectionWrapper::class.java)
            return collectionWrapper
                    ?.collection
                    ?.items
                    ?.map { item -> item.links }
                    ?.flatten()
                    ?.filter { item -> "preview".equals(item.rel) }
                    ?.map { item -> item.href }
                    .orEmpty()
        }
}