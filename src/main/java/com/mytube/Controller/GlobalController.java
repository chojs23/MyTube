package com.mytube.Controller;


import com.mytube.exception.MemberNotFoundException;
import com.mytube.exception.PostNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalController {

    @ExceptionHandler(MemberNotFoundException.class)
    public String handleMemberNotFoundException(){
        return "redirect:/";
    }
    @ExceptionHandler(PostNotFoundException.class)
    public String handlePostNotFoundException(){
        return "redirect:/posts";
    }
}
