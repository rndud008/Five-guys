package com.lec.spring.controller;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Post;
import com.lec.spring.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
// TODO CKEDITOR 추가(WRITE, UPDATE), 첨부파일(UPDATE, DETAIL, WRITE), 좋아요 기능(DETAIL, LIST), 페이징(지역별 페이징)
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;


    @GetMapping("/write")
    public void write(){}

    @PostMapping("/write")
    public String writeOk(Post post, Model model){

        model.addAttribute("result", boardService.write(post));
        return "board/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){

        model.addAttribute("post", boardService.detail(id));
        return "board/detail";
    }

    @GetMapping("/list")
    public void list(Integer page, Model model){
        model.addAttribute("list", boardService.list(page, model));
        model.addAttribute("areacode", boardService.findAllAreaName());
    }

    @PostMapping("/list")
    public String listArea(@RequestParam("areacode") Long areacode, Model model){
        model.addAttribute("list", boardService.listByAreacode(areacode));
        model.addAttribute("selectareacode", boardService.selectNameByAreacode(areacode));
        model.addAttribute("areacode", boardService.findAllAreaName());

        return "board/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        model.addAttribute("post", boardService.selectById(id));

        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(Post post, Model model){
        model.addAttribute("result", boardService.update(post));

        return "board/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){

        model.addAttribute("result", boardService.deleteById(id));

        return "board/deleteOk";
    }


}
