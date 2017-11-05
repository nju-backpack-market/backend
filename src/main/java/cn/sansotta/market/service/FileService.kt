package cn.sansotta.market.service

import org.springframework.web.multipart.MultipartFile

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface FileService {
    fun handleImage(image: MultipartFile): String?
}