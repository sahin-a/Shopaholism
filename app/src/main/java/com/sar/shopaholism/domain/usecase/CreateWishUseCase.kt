package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.repository.WishesRepository

class CreateWishUseCase(private val repo: WishesRepository) {

    suspend fun execute(title: String, description: String, price: Double, priority: Int) {
        require(title.isNotBlank()) { "Wish title can't be blank" }

        val newWish = Wish(id = 0, title, description, price, priority)
        val id = repo.create(newWish)

        if (id <= 0) {
            throw WishNotCreatedException("Wish couldn't be created")
        }
    }
}