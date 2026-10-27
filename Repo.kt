package app.manhwarex.data

import android.content.Context
import android.net.Uri
import app.manhwarex.util.ZipImporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class Repo(
    private val context: Context,
    private val mangaDao: MangaDao,
    private val chapterDao: ChapterDao
) {

    fun getLibraryMangas() = mangaDao.getFavoriteMangas()

    suspend fun importLocalComic(uri: Uri, title: String): String = withContext(Dispatchers.IO) {
        val outputDir = File(context.filesDir, "comics/${System.currentTimeMillis()}")
        outputDir.mkdirs()

        val extractedPages = ZipImporter.importComicArchive(context, uri, outputDir)
        val mangaId = "local_${System.currentTimeMillis()}"
        
        val manga = MangaEntity(
            id = mangaId,
            title = title,
            author = "Lokal / Arsip",
            description = "Komik yang diimpor dari penyimpanan perangkat.",
            coverUrl = extractedPages.firstOrNull()?.absolutePath,
            sourceId = "local",
            status = 1,
            isFavorite = true
        )
        mangaDao.insertManga(manga)

        val chapter = ChapterEntity(
            id = "${mangaId}_ch1",
            mangaId = mangaId,
            title = "Chapter 1",
            chapterNumber = 1f,
            scanlator = "Lokal",
            uploadDate = System.currentTimeMillis(),
            pageCount = extractedPages.size
        )
        chapterDao.insertChapter(chapter)

        return@withContext mangaId
    }
}
