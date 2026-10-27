package app.manhwarex.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mangas")
data class MangaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String?,
    val description: String?,
    val coverUrl: String?,
    val sourceId: String,
    val status: Int,
    val isFavorite: Boolean = false,
    val lastReadChapterId: String? = null,
    val lastPageRead: Int = 0
)

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey val id: String,
    val mangaId: String,
    val title: String,
    val chapterNumber: Float,
    val scanlator: String?,
    val uploadDate: Long,
    val isRead: Boolean = false,
    val pageCount: Int = 0
)
