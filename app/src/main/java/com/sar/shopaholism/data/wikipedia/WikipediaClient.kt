package com.sar.shopaholism.data.wikipedia

import com.sar.shopaholism.data.wikipedia.api.WikipediaApiProvider
import com.sar.shopaholism.domain.entity.WikiPage

class WikipediaClient(
    private val wikipediaApiProvider: WikipediaApiProvider
) {
    suspend fun getPage(title: String, limit: Int): List<WikiPage> {
        val searchResultsDto = wikipediaApiProvider.searchPages(title, limit)
        return searchResultsDto.pages.map { searchResult ->
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
    }
}