package com.lec.spring.controller;

import com.lec.spring.service.CommentLikeService;
import com.lec.spring.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentLikeService commentLikeService;

    // 게시판 좋아요
    @PostMapping("/click")
    public void click(@RequestParam("user_id") Long userId, @RequestParam("travel_diary_post_id") Long postId){

        likeService.likeClick(userId,postId);
    }
    @GetMapping("/count/{postId}")
    public Long count(@PathVariable Long postId){
        Long result = 0L;
        result = likeService.likeCount(postId);

        return result;
    }

    // 댓글 좋아요
    @PostMapping("/clickC")
    public void clickC(@RequestParam("user_id") Long userId, @RequestParam("comment_id") Long commentId){
        commentLikeService.likeClick(userId, commentId);
    }

    @GetMapping("/countC/{commentId}")
    public Long countC(@PathVariable Long commentId){
        Long result = 0L;
        result = commentLikeService.likeCount(commentId);

        return result;
    }


}
