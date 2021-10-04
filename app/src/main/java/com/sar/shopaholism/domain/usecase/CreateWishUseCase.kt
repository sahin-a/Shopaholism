package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.WishesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateWishUseCase(private val repo: WishesRepository, private val logger: Logger) {
    companion object {
        const val TAG: String = "CreateWishUseCase"
    }

    suspend fun execute(title: String, imageUri: String, description: String, price: Double)
    = withContext(Dispatchers.IO) {
        WishValidation.validate(
            title = title,
            price = price,
            priority = 0
        )

        val id: Long = repo.create(Wish(id = 0L, imageUri, title, description, price, priority = 0))

        if (id <= 0L) {
            logger.d(
                tag = TAG,
                message = "Failed to add new wish to local db"
            )

            throw WishNotCreatedException("Wish couldn't be created")
        }

        logger.i(
            tag = TAG,
            message = "New Wish has been added to the local Db"
        )
    }
}