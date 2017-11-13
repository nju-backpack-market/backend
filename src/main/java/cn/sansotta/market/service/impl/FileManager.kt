package cn.sansotta.market.service.impl

import cn.sansotta.market.service.FileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@Service
class FileManager(@Value("${'$'}{upload.image.location}") imageLocation: String,
                  @Value("${'$'}{upload.video.location}") videoLocation: String) : FileService {
    private val imageDir = Files.createDirectories(Paths.get(imageLocation)).toFile()
    private val videoDir = Files.createDirectories(Paths.get(videoLocation)).toFile()
    private val nextId get() = UUID.randomUUID().toString()

    override fun handleImage(image: MultipartFile) = transferTo(image, imageDir)

    override fun handleVideo(video: MultipartFile) = transferTo(video, videoDir)

    private fun transferTo(file: MultipartFile, dir: File) =
            try {
                val index = file.originalFilename.lastIndexOf('.')
                if (index < 0) null
                else File(dir, nextId + file.originalFilename.substring(index))
                        .also { file.transferTo(it) }
                        .also { it.setExecutable(false) }
                        .name
            } catch (ex: Exception) {
                ex.printStackTrace()
                ""
            }
}
