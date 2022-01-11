package com.sar.shopaholism.presentation.feedback

import com.sar.shopaholism.R

class WishFeedbackService(
    private val soundProvider: SoundProvider
) {
    fun wishSuccessfullyCreated() {
        soundProvider.play(R.raw.alert_high_intensity)
    }

    fun wishSuccessfullyDeleted() {
        soundProvider.play(R.raw.alert_high_intensity)
    }

    fun wishSuccessfullyModified() {
        soundProvider.play(R.raw.alert_high_intensity)
    }

    fun wishSuccessfullyRated() {
        soundProvider.play(R.raw.alert_high_intensity)
    }
}