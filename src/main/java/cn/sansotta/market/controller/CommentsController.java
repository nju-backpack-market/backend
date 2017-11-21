package cn.sansotta.market.controller;

import cn.sansotta.market.controller.resource.ProductResource;
import cn.sansotta.market.domain.value.Comment;
import cn.sansotta.market.domain.value.Product;
import cn.sansotta.market.service.Authorized;
import cn.sansotta.market.service.CommentService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.sansotta.market.common.WebUtils.HAL_MIME_TYPE;
import static cn.sansotta.market.common.WebUtils.notFoundResponse;
import static cn.sansotta.market.common.WebUtils.toResponse;

/**
 * @author Hiki
 * @create 2017-11-21 18:31
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

	@Autowired
	private CommentService commentService;

	@GetMapping(value = "/product/{pid}")
	public PageInfo<Comment>
	getCommentsByProductId(@RequestParam("pageNum") int pageNum, @PathVariable("pid") long pid) {
		return commentService.getCommentsByProductId(pageNum, pid);
	}

	@GetMapping(value = "/product/{pid}/order/{oid}")
	public Comment getCommentByOrderIdAndProductId(@RequestParam("pageNum") int pageNum,
												   @PathVariable("pid") long pid,
												   @PathVariable("oid") long oid){
		return commentService.getCommentByOrderIdAndProductId(oid, pid);
	}

	@Authorized
	@PostMapping()
	public List<Comment> addComments(@RequestBody List<Comment> comments){
		return commentService.addComments(comments);
	}

	@Authorized
	@PostMapping()
	public Comment addComment(@RequestBody Comment comment){
		return commentService.addComment(comment);
	}

}
