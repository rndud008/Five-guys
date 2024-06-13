package com.lec.spring.controller;

import com.lec.spring.domain.Post;
import com.lec.spring.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public void list(Model model){
        model.addAttribute("list", boardService.list());
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
