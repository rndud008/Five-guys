package com.lec.spring.controller;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Post;
import com.lec.spring.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

// TODO CKEDITOR 추가(WRITE, UPDATE), 첨부파일(UPDATE, DETAIL, WRITE), 좋아요 기능(DETAIL, LIST)
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;


    @GetMapping("/write")
    public void write(Long areacode, Model model){
        model.addAttribute("areacodelist", boardService.findAllArea());
        model.addAttribute("selectedArea", areacode);
    }

    @PostMapping("/write")
    public String writeOk(@RequestParam Map<String, MultipartFile> files, Post post, Model model){
        // TODO files 가 안넘어옴 파일 데이터에 안드감
        // TODO 수정페이지 파일 추가, 삭제 x
        System.out.println(post);
        model.addAttribute("result", boardService.write(post, files));
        return "board/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        Post post =  boardService.detail(id);
        model.addAttribute("post", post);
        model.addAttribute("areacode", boardService.findAreaByAreacode(post.getAreacode_id()));
        return "board/detail";
    }

    @GetMapping("/list")
    public void list(Integer page, Model model){
        model.addAttribute("list", boardService.list(page, model));
        model.addAttribute("areacode", boardService.findAllArea());
    }


    @GetMapping("/listArea")
    public String listArea(Integer page, @RequestParam("areacode") Long areacode, Model model){

        model.addAttribute("list", boardService.listByAreacode(page, areacode, model));
        model.addAttribute("selectareacode", boardService.findAreaByAreacode(areacode));
        model.addAttribute("areacode", boardService.findAllArea());


        return "board/listArea";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        model.addAttribute("post", boardService.selectById(id));
        model.addAttribute("areacodelist", boardService.findAllArea());
        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(Map<String, MultipartFile> files, Post post, Long[] delfile, Model model){
        model.addAttribute("result", boardService.update(post, files, delfile));

        return "board/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){

        model.addAttribute("result", boardService.deleteById(id));

        return "board/deleteOk";
    }


}
