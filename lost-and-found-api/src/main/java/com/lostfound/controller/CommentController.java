package com.lostfound.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lostfound.common.Result;
import com.lostfound.entity.Comment;
import com.lostfound.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{itemId}")
    public Result<List<Comment>> list(@PathVariable Long itemId, @RequestParam String itemType) {
        List<Comment> list = commentService.list(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getItemId, itemId)
                        .eq(Comment::getItemType, itemType)
                        .orderByAsc(Comment::getCreateTime)
        );
        return Result.success(list);
    }

    @PostMapping
    public Result<?> add(@RequestBody Comment comment, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        comment.setUserId(userId);
        commentService.save(comment);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        commentService.removeById(id);
        return Result.success();
    }
}
