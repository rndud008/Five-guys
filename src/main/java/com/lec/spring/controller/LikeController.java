package com.lec.spring.controller;

import com.lec.spring.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/click")
    public void click(@RequestParam("user_id") Long userId, @RequestParam("travel_diary_post_id") Long postId){

        likeService.likeClick(userId,postId);
    }
    @GetMapping("/count/{postId}")
    public Long count(@PathVariable Long postId){
        Long result = 0L;
        result = likeService.likeCount(postId);

        System.out.println("result ==================================" + result);
        return result;
    }


}
