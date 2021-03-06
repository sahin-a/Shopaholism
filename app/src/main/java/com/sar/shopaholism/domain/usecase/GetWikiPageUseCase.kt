package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.data.wikipedia.WikipediaClient
import com.sar.shopaholism.data.wikipedia.exceptions.WikiPageRetrievalFailedException
import com.sar.shopaholism.data.wikipedia.exceptions.WikiPageSearchFailedException
import com.sar.shopaholism.domain.entity.WikiPage
import com.sar.shopaholism.domain.logger.Logger

class GetWikiPageUseCase(
    private val wikipediaClient: WikipediaClient,
    private val logger: Logger
) {
    // TODO: Get limit from storage
    suspend fun execute(title: String, limit: Int = 20): List<WikiPage> {
        try {
            return wikipediaClient.getPages(title, limit)
        } catch (e: WikiPageSearchFailedException) {
            logger.w(TAG, "Wiki search failed for $title")
        } catch (e: WikiPageRetrievalFailedException) {
            logger.w(TAG, "Wiki Page retrieval failed for $title")
        }

        return listOf()
    }

    companion object {
        private const val TAG = "GetWikiPageUseCase"
    }
}