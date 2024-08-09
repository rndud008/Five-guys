package com.lec.spring.controller;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Post;
import com.lec.spring.domain.PostValidator;
import com.lec.spring.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

// TODO CKEDITOR 추가(WRITE, UPDATE) VALIDATOR 추가(제목필수)
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);
    }



    @GetMapping("/write")
    public void write(Long areacode, Model model){
        model.addAttribute("areacodelist", boardService.findAllArea());
        model.addAttribute("selectedArea", areacode);
    }

    @PostMapping("/write")
    public String writeOk(
            @RequestParam Map<String, MultipartFile> files
            , @Valid Post post
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttributes
    ){
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("user", post.getUser());
            redirectAttributes.addFlashAttribute("subject", post.getSubject());
            redirectAttributes.addFlashAttribute("content", post.getContent());

            List<FieldError> errorList = result.getFieldErrors();
            for(FieldError err : errorList){
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
            }
            return "redirect:/board/write";
        }

        model.addAttribute("result", boardService.write(post, files));
        return "board/writeOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        Post post =  boardService.detail(id);
        model.addAttribute("post", post);
        model.addAttribute("areacode", boardService.findAreaByAreacode(post.getAreacode()));
        return "board/detail";
    }

    @GetMapping("/list")
    public void list(Integer page, Long areacode, Model model){

        model.addAttribute("list", boardService.list(page, areacode, model));
        model.addAttribute("areacode", boardService.findAllArea());
        model.addAttribute("selectareacode", boardService.findAreaByAreacode(areacode));

    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model){
        model.addAttribute("post", boardService.selectById(id));
        model.addAttribute("areacodelist", boardService.findAllArea());
        return "board/update";
    }

    @PostMapping("/update")
    public String updateOk(
            @RequestParam Map<String, MultipartFile> files
            , @Valid Post post
            , BindingResult result
            , Long[] delfile
            , Model model
            , RedirectAttributes redirectAttributes

    ){
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("subject", post.getSubject());
            redirectAttributes.addFlashAttribute("content", post.getContent());

            List<FieldError> errList = result.getFieldErrors();
            for(FieldError err : errList){
                redirectAttributes.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/board/update/" + post.getId();
        }
        model.addAttribute("result", boardService.update(post, files, delfile));

        return "board/updateOk";
    }

    @PostMapping("/delete")
    public String deleteOk(Long id, Model model){

        model.addAttribute("result", boardService.deleteById(id));

        return "board/deleteOk";
    }

    @InitBinder("post")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator((new PostValidator()));
    }


}
