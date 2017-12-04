package cn.sansotta.market.service.impl

import cn.sansotta.market.common.commonPool
import cn.sansotta.market.service.FileService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import javax.annotation.PostConstruct

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
@Profile("dir")
class FileManager(@Value("${'$'}{upload.image}") imageLocation: String,
                  @Value("${'$'}{upload.video}") videoLocation: String) : FileService {
    private val imageDir = Files.createDirectories(Paths.get(imageLocation)).toFile()
    private val videoDir = Files.createDirectories(Paths.get(videoLocation)).toFile()
    private val nextId get() = UUID.randomUUID().toString()
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun startCleaner() {
        commonPool.submit(this::cleanImage)
        commonPool.submit(this::cleanVideo)
    }

    private fun cleanImage() {
        while (true) doDelete(File(imageDir, deletedImages.take()))
    }

    private fun cleanVideo() {
        while (true) doDelete(File(videoDir, deletedImages.take()))
    }

    private fun doDelete(file: File) {
        try {
            file.takeIf(File::isFile)?.delete()
        } catch (ex: Exception) {
            logger.error("error when clean file $file", ex)
        }
    }

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

    @Suppress("UNUSED")
    companion object {
        private val deletedImages = ArrayBlockingQueue<String>(20)
        private val deletedVideos = ArrayBlockingQueue<String>(10)

        fun deleteImage(name: String) {
            deletedImages.offer(name)
        }

        fun deleteImage(names: Iterable<String>) {
            names.forEach { deletedImages.offer(it) }
        }

        fun deleteVideo(name: String) {
            deletedVideos.offer(name)
        }

        fun deleteVideo(names: Iterable<String>) {
            names.forEach { deletedVideos.offer(it) }
        }
    }
}
