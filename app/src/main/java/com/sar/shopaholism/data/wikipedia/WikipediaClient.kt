package com.sar.shopaholism.data.wikipedia

import com.sar.shopaholism.data.wikipedia.api.WikipediaApiProvider
import com.sar.shopaholism.domain.entity.WikiPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class WikipediaClient(
    private val wikipediaApiProvider: WikipediaApiProvider
) {
    suspend fun getPages(title: String, limit: Int): List<WikiPage> = coroutineScope {
        val searchResultsDto = wikipediaApiProvider.searchPages(title, limit)

        return@coroutineScope searchResultsDto.pages.map { searchResult ->
            async(Dispatchers.IO) {
                searchResult.thumbnail?.apply {
                    if (url.startsWith("//")) {
                        url = url.replaceFirst("//", "https://")
                    }
                }
                val page = wikipediaApiProvider.getPage(searchResult.title)

                WikiPage(
                    title = page.title,
                    description = searchResult.description ?: "",
                    thumbnailUrl = searchResult.thumbnail?.url ?: "",
                    htmlUrl = page.html_url
                )
            }
        }.awaitAll()
    }
}