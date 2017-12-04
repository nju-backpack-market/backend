package cn.sansotta.market.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.sansotta.market.service.Authorized;
import cn.sansotta.market.service.FileService;

import static cn.sansotta.market.common.WebUtils.badRequestResponse;
import static cn.sansotta.market.common.WebUtils.insufficientStorageResponse;
import static cn.sansotta.market.common.WebUtils.singletonHeader;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("dir")
@RestController
@RequestMapping("/files")
public class FilesController {
    private final FileService fileService;

    public FilesController(FileService fileService) {this.fileService = fileService;}

    @Authorized
    @PostMapping(value = "/{type}", consumes = "multipart/form-data")
    public ResponseEntity<String>
    uploadImage(@PathVariable("type") String type,
                @RequestParam("file") MultipartFile file) {
        String name = null;
        switch (type) {
            case "image":
                name = fileService.handleImage(file);
                break;
            case "video":
                name = fileService.handleVideo(file);
                break;
        }

        if(name == null) return badRequestResponse();
        if(name.isEmpty()) return insufficientStorageResponse();
        return new ResponseEntity<>(
                name,
                singletonHeader("Location", String.format("/static/%s/%s", type, name)),
                HttpStatus.CREATED);
    }
}
