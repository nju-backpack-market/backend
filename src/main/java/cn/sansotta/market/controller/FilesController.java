package cn.sansotta.market.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.sansotta.market.service.FileService;

import static cn.sansotta.market.common.HateoasUtils.badRequestResponse;
import static cn.sansotta.market.common.HateoasUtils.insufficientStorageResponse;
import static cn.sansotta.market.common.HateoasUtils.singletonHeader;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@RequestMapping("/files")
public class FilesController {
    private final FileService fileService;

    public FilesController(FileService fileService) {this.fileService = fileService;}

    @PostMapping(value = "/{type}", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImage(@PathVariable("type") String type,
                                              @RequestParam("image") MultipartFile image) {
        switch (type) {
            case "image":
                String name = fileService.handleImage(image);
                if(name == null) return insufficientStorageResponse();
                return new ResponseEntity<>(singletonHeader("Location", "/static/image/" + name),
                        HttpStatus.OK);
            default:
                return badRequestResponse();
        }
    }
}
