package cn.sansotta.market.controller;

import com.github.pagehelper.PageInfo;

import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import cn.sansotta.market.domain.value.Comment;
import cn.sansotta.market.service.CommentService;

import static cn.sansotta.market.common.WebUtils.HAL_MIME_TYPE;
import static cn.sansotta.market.common.WebUtils.JSON_MIME_TYPE;
import static cn.sansotta.market.common.WebUtils.notFoundResponse;
import static cn.sansotta.market.common.WebUtils.pagedResources;
import static cn.sansotta.market.common.WebUtils.toResponse;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author Hiki
 */
@Profile("!dev_test")
@RestController
@RequestMapping("/comments")
public class CommentsController {
    private final CommentService commentService;

    public CommentsController(CommentService commentService) {this.commentService = commentService;}

    @GetMapping(produces = HAL_MIME_TYPE)
    public ResponseEntity<PagedResources<Comment>>
    comments(@RequestParam(value = "pid") long pid,
             @RequestParam(value = "oid", required = false, defaultValue = "-1") long oid,
             @RequestParam(value = "pageNum", required = false, defaultValue = "0") int pageNum) {
        PagedResources<Comment> resources;
        if(oid > 0L) resources = assembleResources(commentService.comment(pid, oid), pid, oid);
        else resources = assembleResources(commentService.commentsOfProduct(pid, pageNum), pid, oid);
        return resources == null ? notFoundResponse() : toResponse(resources);
    }

    @PostMapping(consumes = JSON_MIME_TYPE, produces = HAL_MIME_TYPE)
    public ResponseEntity<List<Comment>> addComments(@RequestBody List<Comment> comments) {
        return toResponse(commentService.addComments(comments));
    }

    private PagedResources<Comment> assembleResources(Comment comment, long pid, long oid) {
        if(comment == null) return null;
        return assembleResources(new PageInfo<>(Collections.singletonList(comment)), pid, oid);
    }

    private PagedResources<Comment>
    assembleResources(PageInfo<Comment> info, long pid, long oid) {
        if(info == null) return null;
        PagedResources<Comment> resources = pagedResources(info);
        resources.add(
                linkTo(methodOn(getClass()).comments(pid, oid, info.getPageNum())).withSelfRel(),
                linkTo(methodOn(getClass()).comments(pid, oid, info.getNavigateFirstPage()))
                        .withRel("first"),
                linkTo(methodOn(getClass()).comments(pid, oid, info.getNavigateLastPage()))
                        .withRel("last")
        );
        if(info.isHasNextPage())
            resources.add(linkTo(methodOn(getClass()).comments(pid, oid, info.getPrePage()))
                    .withRel("prev"));
        if(info.isHasNextPage())
            resources.add(linkTo(methodOn(getClass()).comments(pid, oid, info.getNextPage()))
                    .withRel("next"));
        return resources;
    }
}
