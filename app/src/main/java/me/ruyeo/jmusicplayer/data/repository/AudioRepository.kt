package me.ruyeo.jmusicplayer.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.ruyeo.jmusicplayer.data.ContentResolverHelper
import me.ruyeo.jmusicplayer.model.Audio
import javax.inject.Inject

class AudioRepository @Inject constructor(
    private val contentResolverHelper: ContentResolverHelper
) {
    suspend fun getAudioData(): List<Audio> = withContext(Dispatchers.IO){
        contentResolverHelper.getAudioData()
    }

}