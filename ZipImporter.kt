package app.manhwarex.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

object ZipImporter {

    private val SUPPORTED_IMAGE_EXTENSIONS = listOf("jpg", "jpeg", "png", "webp")

    fun importComicArchive(context: Context, uri: Uri, outputDir: File): List<File> {
        val extractedImages = mutableListOf<File>()
        val inputStream = context.contentResolver.openInputStream(uri) ?: return emptyList()
        
        ZipInputStream(inputStream).use { zipStream ->
            var entry = zipStream.nextEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    val fileName = entry.name.lowercase()
                    val extension = fileName.substringAfterLast('.', "")
                    
                    if (SUPPORTED_IMAGE_EXTENSIONS.contains(extension)) {
                        val outputFile = File(outputDir, File(entry.name).name)
                        outputFile.parentFile?.mkdirs()
                        
                        FileOutputStream(outputFile).use { output ->
                            val buffer = ByteArray(8192)
                            var len: Int
                            while (zipStream.read(buffer).also { len = it } > 0) {
                                output.write(buffer, 0, len)
                            }
                        }
                        extractedImages.add(outputFile)
                    }
                }
                zipStream.closeEntry()
                entry = zipStream.nextEntry
            }
        }
        
        return extractedImages.sortedWith { f1, f2 -> 
            NaturalOrder.compare(f1.name, f2.name) 
        }
    }
}
