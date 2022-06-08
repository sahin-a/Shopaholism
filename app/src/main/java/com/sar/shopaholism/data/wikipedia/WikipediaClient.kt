package com.sar.shopaholism.data.wikipedia

import com.sar.shopaholism.data.wikipedia.api.WikipediaApiProvider
import com.sar.shopaholism.data.wikipedia.dto.SearchResultDto
import com.sar.shopaholism.domain.entity.WikiPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class WikipediaClient(
    private val wikipediaApiProvider: WikipediaApiProvider
) {
    private fun String.addHttps() = this.replaceFirst("^//|http://".toRegex(), "https://")

    private suspend fun getPage(searchResult: SearchResultDto) = coroutineScope {
        val page = wikipediaApiProvider.getPage(searchResult.title)
        WikiPage(
            title = page.title,
            description = searchResult.description ?: "",
            thumbnailUrl = searchResult.thumbnail?.url?.addHttps() ?: "",
            htmlUrl = page.html_url
        )
    }

    suspend fun getPages(title: String, limit: Int): List<WikiPage> = coroutineScope {
        val searchResultsDto = wikipediaApiProvider.searchPages(title, limit)
        return@coroutineScope searchResultsDto.pages
            .map { async(Dispatchers.IO) { getPage(it) } }
            .awaitAll()
    }
}