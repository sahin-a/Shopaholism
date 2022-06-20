package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

data class Vote(
    val wish: Wish,
    val isPreferred: Boolean
)

data class VoteSummary(
    val wish: Wish,
    val votes: List<Vote>
)

class UpdateWishPriorityByVotesUseCase(
    private val updateWishUseCase: UpdateWishUseCase
) {

    private fun calculatePriority(summary: VoteSummary): Double {
        val positiveVotes = summary.votes.count { !it.isPreferred }.toDouble()
        val totalVotes = summary.votes.count().toDouble()
        return (positiveVotes / totalVotes) * 100
    }

    /**
     * Updates the wish priority using the data from the vote
     *
     * @param summary the data collected from the vote
     */
    suspend fun execute(summary: VoteSummary) = coroutineScope {
        summary.wish.priority = calculatePriority(summary)
        val wishesWithIdenticalPriority =
            summary.votes.filter { it.wish.priority == summary.wish.priority }
                .map { it.wish }

        launch {
            updateWishUseCase.execute(summary.wish)
        }

        // decreases the priority of wishes with identical priority as the just voted one
        wishesWithIdenticalPriority.forEach { wish ->
            launch {
                wish.priority--
                updateWishUseCase.execute(wish)
            }
        }
    }
}