package com.sar.shopaholism.data.wikipedia.api

import com.sar.shopaholism.data.serizaliation.JsonDeserializer
import com.sar.shopaholism.data.web.HttpMethod
import com.sar.shopaholism.data.web.WebApiClient
import com.sar.shopaholism.data.web.exceptions.WebRequestUnsuccessfulException
import com.sar.shopaholism.data.web.model.Request
import com.sar.shopaholism.data.wikipedia.dto.PageDto
import com.sar.shopaholism.data.wikipedia.dto.SearchResultsDto
import com.sar.shopaholism.data.wikipedia.exceptions.WikiPageRetrievalFailedException
import com.sar.shopaholism.data.wikipedia.exceptions.WikiPageSearchFailedException

class WikipediaApiProvider(
    private val webClient: WebApiClient
) {
    init {
        webClient.setBasePath("https://en.wikipedia.org/w/rest.php/v1")
    }

    suspend fun searchPages(title: String, limit: Int): SearchResultsDto {
        val request = Request(
            path = "/search/page",
            method = HttpMethod.GET,
            parameters = mapOf(
                "q" to title,
                "limit" to limit.toString()
            )
        )

        try {
            return webClient.sendRequest(request, JsonDeserializer(), SearchResultsDto::class.java)
        } catch (e: WebRequestUnsuccessfulException) {
            throw WikiPageSearchFailedException("Failed to retrieve search results", e)
        }
    }

    suspend fun getPage(pageTitle: String): PageDto {
        val request = Request(
            path = "/page/$pageTitle/bare",
            method = HttpMethod.GET,
            parameters = mapOf()
        )

        try {
            return webClient.sendRequest(request, JsonDeserializer(), PageDto::class.java)
        } catch (e: WebRequestUnsuccessfulException) {
            throw WikiPageRetrievalFailedException("Failed to retrieve page", e)
        }
    }
}