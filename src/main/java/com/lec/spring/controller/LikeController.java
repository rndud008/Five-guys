package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetailService;
import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.service.CommentLikeService;
import com.lec.spring.service.LikeService;
import com.lec.spring.service.TravelPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;
    @Autowired
    private TravelPostService travelPostService;
    @Autowired
    private CommentLikeService commentLikeService;

    // 게시판 좋아요
    @PostMapping("/click")
    public int click(@RequestParam("user_id") Long userId, @RequestParam("travel_diary_post_id") Long postId){
        return likeService.likeClick(userId,postId);
    }
    @PostMapping("/likeChk")
    public int likeChk(@RequestParam("user_id") Long userId, @RequestParam("travel_diary_post_id") Long postId){
        return likeService.likeChk(userId, postId);
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

    // 댓글 좋아요 찾기
    @PostMapping("/cLikeChk")
    public int clickChk(@RequestParam("user_id") Long userId, @RequestParam("comment_id") Long commentId){
        return commentLikeService.likeChk(userId, commentId);
    }


    @GetMapping("/countC/{commentId}")
    public Long countC(@PathVariable Long commentId){
        Long result = 0L;
        result = commentLikeService.likeCount(commentId);

        return result;
    }

    @GetMapping("/getTravelPostLike")
    public ResponseEntity<?> getTravelPostLike(@RequestParam("travelPostId")Long postId, @AuthenticationPrincipal UserDetails userDetails){

        Long userId = getUserIdFromUserDetails(userDetails);

        if (userId == null){
            return ResponseEntity.ok(" ");
        }

        Long result = travelPostService.selectedLike(postId,userId);

        if (result == 1) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok(" ");
        }
    }

    @PostMapping("/postTravelPostLike")
    public ResponseEntity<?> postTravelPostLike( @RequestParam("travelPostId")Long postId, @AuthenticationPrincipal UserDetails userDetails){
        if(userDetails == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        Long userId = getUserIdFromUserDetails(userDetails);

        int result;

        if(travelPostService.selectedLike(postId,userId) == 0){
         result = travelPostService.saveLike(postId,userId);
        }else {
            travelPostService.deleteLike(postId,userId);
            return ResponseEntity.ok(' ');
        }

        if(result > 0){
            return ResponseEntity.ok(result);
        }else {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update like status");
        }

    }

    @GetMapping("/travelPostLikeCount")
    public ResponseEntity<?> getTravelPostLikeCount(@RequestParam("travelPostId")Long postId){
        Long result = travelPostService.totalLike(postId);
        System.out.println(result);

        return ResponseEntity.ok(result);

    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        if(userDetails instanceof PrincipalDetails){
            return ((PrincipalDetails) userDetails).getUser().getId();
        }

        return null;
    }

}
