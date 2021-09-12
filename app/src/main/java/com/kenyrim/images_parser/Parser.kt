package com.kenyrim.images_parser

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class Parser {
    suspend fun run(url: String, selector: String): List<String> {
        val data: ArrayList<String> = ArrayList()
        val parse: Deferred<List<String>?> = CoroutineScope(Dispatchers.IO).async {
            val doc = Jsoup.connect(url).get()
            val metaElements: Elements = doc.select(selector)

            metaElements.forEach {
                val item = it.select("img").attr("src")
                if (item.isNotEmpty())
                data.add(item)
            }
            data
        }
        parse.await()
        return data
    }
}