package com.sar.shopaholism.data.wikipedia

import com.sar.shopaholism.data.wikipedia.api.WikipediaApiProvider
import com.sar.shopaholism.domain.entity.WikiPage
import kotlinx.coroutines.*

class WikipediaClient(
    private val wikipediaApiProvider: WikipediaApiProvider
) {
    suspend fun getPage(title: String, limit: Int): List<WikiPage> = withContext(
        Dispatchers.IO
    ) {
        val searchResultsDto = wikipediaApiProvider.searchPages(title, limit)
        val wikiPages = searchResultsDto.pages.map { searchResult ->
            searchResult.thumbnail?.apply {
                if (url.startsWith("//")) {
                    url = url.replaceFirst("//", "https://")
                }
            }

            return@map async {
                val page = wikipediaApiProvider.getPage(searchResult.title)

                return@async WikiPage(
                    title = page.title,
                    description = searchResult.description ?: "",
                    thumbnailUrl = searchResult.thumbnail?.url ?: "",
                    htmlUrl = page.html_url
                )
            }
        }

        return@withContext wikiPages.awaitAll()
    }
}