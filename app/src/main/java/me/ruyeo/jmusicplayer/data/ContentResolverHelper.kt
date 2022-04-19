package me.ruyeo.jmusicplayer.data

import android.content.ContentUris
import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import me.ruyeo.jmusicplayer.model.Audio
import javax.inject.Inject

class ContentResolverHelper @Inject constructor(
    @ApplicationContext var context: Context
) {
    private var mCursor: Cursor? = null

    private val projection: Array<String> = arrayOf(
        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.TITLE
    )

    private var selectionClause: String? =
        "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"

    private var selectionArgs = arrayOf("1")

    private val sortOrder = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"

    fun getAudioData(): List<Audio> {
        return getCursorData()
    }

    private fun getCursorData(): MutableList<Audio> {
        val audioList = mutableListOf<Audio>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClause,
            selectionArgs,
            sortOrder
        )

        mCursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val displayColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)

            cursor.apply {
                if (count == 0) {
                    Log.d(TAG, "getCursorData: Cursor is Empty")
                } else {
                    while (cursor.moveToNext()) {
                        val displayName = getString(displayColumn)
                        val id = getLong(idColumn)
                        val title = getString(titleColumn)
                        val artist = getString(artistColumn)
                        val data = getString(dataColumn)
                        val duration = getInt(durationColumn)
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                        )

                        audioList += Audio(
                            uri, displayName, id, artist, data, duration, title
                        )
                    }
                }
            }
        }
        return audioList
    }

}