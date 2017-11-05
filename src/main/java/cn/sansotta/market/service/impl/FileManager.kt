package cn.sansotta.market.service.impl

import cn.sansotta.market.service.FileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Component
class FileManager(@Value("${'$'}{upload.image.location}") imageLocation: String) : FileService {
    private val imageDir = Files.createDirectories(Paths.get(imageLocation)).toFile()
    private val nextId get() = UUID.randomUUID().toString()

    override fun handleImage(image: MultipartFile) =
            try {
                File(imageDir, nextId + image.name.substring(image.name.lastIndexOf('.')))
                        .also { image.transferTo(it) }
                        .also { it.setExecutable(false) }
                        .name
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
}
