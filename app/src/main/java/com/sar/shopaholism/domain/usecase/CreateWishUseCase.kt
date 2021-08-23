package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.WishesRepository

class CreateWishUseCase(private val repo: WishesRepository, private val logger: Logger) {

    suspend fun execute(title: String, description: String, price: Double) {
        WishValidation.validate(
            title = title,
            description = description,
            price = price,
            priority = 0
        )

        val id = repo.create(Wish(id = 0, title, description, price, priority = 0))

        if (id <= 0) {
            logger.e(
                tag = this::class.java.name,
                message = "Failed to add new wish to local db",
                throwable = throw WishNotCreatedException("Wish couldn't be created")
            )
        }

        logger.i(
            tag = this::class.java.name,
            message = "New Wish has been added to the local Db"
        )
    }
}