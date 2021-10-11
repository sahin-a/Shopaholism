package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.WishesRepository
import java.math.BigDecimal
import java.math.RoundingMode

class CreateWishUseCase(private val repo: WishesRepository, private val logger: Logger) {

    suspend fun execute(title: String, imageUri: String, description: String, price: Double): Long {
        WishValidation.validate(
            title = title,
            price = price,
            priority = 0.0
        )

        val id: Long = repo.create(
            Wish(
                id = 0L,
                imageUri = imageUri,
                title = title,
                description = description,
                price = BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN)
                    .toDouble(),
                priority = 0.0
            )
        )

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

        return id
    }

    companion object {
        const val TAG: String = "CreateWishUseCase"
    }
}