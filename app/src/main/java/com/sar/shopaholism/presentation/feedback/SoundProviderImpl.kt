package com.sar.shopaholism.presentation.feedback

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer

class SoundProviderImpl(
    private val context: Context,
    private val mediaPlayer: MediaPlayer
) : SoundProvider {

    override fun play(resourceId: Int) {
        try {
            context.resources.openRawResourceFd(resourceId).let { afd ->
                mediaPlayer.reset()
                mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        } catch (e: Resources.NotFoundException) {

        }
    }
}