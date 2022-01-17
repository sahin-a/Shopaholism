package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.data.wikipedia.WikipediaClient
import com.sar.shopaholism.domain.entity.WikiPage
import com.sar.shopaholism.domain.logger.Logger

class GetWikiPageUseCase(
    private val wikipediaClient: WikipediaClient,
    private val logger: Logger
) {

    suspend fun execute(title: String, limit: Int = 10): List<WikiPage> {
        return wikipediaClient.getPage(title, limit)
    }
}