package com.sainsburys.imagefinder.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

data class CollectionWrapper(val collection: Collection)
data class Collection(val items: List<Item>)
data class Item(val links: List<Link>?)
data class Link(val rel: String, val href: String)

@RestController
class ImageController {

        @RequestMapping(value = ["/images"], method = [RequestMethod.GET], headers = ["Accept=application/json"])
        fun get(@RequestParam q: String?): List<String> {
            val q = if (!q.isNullOrEmpty()) q else "clouds"
            val collectionWrapper = RestTemplate().getForObject("https://images-api.nasa.gov/search?q=" + q, CollectionWrapper::class.java)
            return collectionWrapper
                    ?.collection
                    ?.items
                    ?.map { item -> item.links ?: ArrayList<Link>() }
                    ?.flatten()
                    ?.filter { item -> "preview".equals(item.rel) }
                    ?.map { item -> item.href }
                    .orEmpty()
        }
}