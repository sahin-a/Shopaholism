package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.repository.WishesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteWishUseCase(
    private val repo: WishesRepository,
    private val updateAllWishesPriorityUseCase: UpdateAllWishesPriorityUseCase
) {

    suspend fun execute(wishId: Long, dispatcher: CoroutineDispatcher = Dispatchers.IO): Unit =
        withContext(dispatcher) {
            WishValidation.validate(wishId = wishId)

            val isDeleted = repo.delete(wishId)

            if (isDeleted) {
                updateAllWishesPriorityUseCase.execute { wishesCount -> wishesCount + 1 }
            } else {
                throw WishNotDeletedException("Wish couldn't be deleted")
            }
        }
}