package com.aloha.exam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aloha.exam.domain.Todo;
import com.aloha.exam.domain.TodoStatus;
import com.aloha.exam.service.inter.TodoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 화면(MVC) 컨트롤러 - Thymeleaf 뷰를 반환한다.
 */
@Slf4j
/* [문제 4-①] 뷰(Thymeleaf)를 반환하는 MVC 컨트롤러로 등록하는 어노테이션을 작성하시오. */
/* [문제 4-②] 이 컨트롤러의 공통 요청 경로를 "/todos" 로 지정하는 어노테이션을 작성하시오. */
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;

  // 목록 화면 : GET /todos?status=TODO
  /* [문제 4-③] GET 방식 요청을 처리하는 어노테이션을 작성하시오. */
  public String list(@RequestParam(value = "status", required = false) TodoStatus status, Model model) {
    /* [문제 4-④] 뷰에서 사용할 수 있도록 todoService.list(status) 결과를 "todos" 라는 이름으로 Model 에 담으시오. */
    model.addAttribute("status", status);
    model.addAttribute("statuses", TodoStatus.values());
    return "todo/list";
  }

  // 등록 화면 : GET /todos/new
  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("todo", new Todo());
    model.addAttribute("statuses", TodoStatus.values());
    return "todo/form";
  }

  // 등록 처리 : POST /todos
  @PostMapping
  public String create(/* [문제 4-⑤] 폼 파라미터를 Todo 객체로 바인딩하는 어노테이션을 작성하시오. */ Todo todo) {
    todoService.create(todo);
    /* [문제 4-⑥] 등록 후 목록 화면(/todos)으로 이동(redirect)하도록 반환값을 작성하시오. */
    return null;
  }

  // 수정 화면 : GET /todos/{no}/edit
  @GetMapping("/{no}/edit")
  public String editForm(/* [문제 4-⑦] 경로의 {no} 값을 받는 어노테이션을 작성하시오. */ Long no, Model model) {
    model.addAttribute("todo", todoService.get(no));
    model.addAttribute("statuses", TodoStatus.values());
    return "todo/form";
  }

  // 수정 처리 : POST /todos/{no}
  @PostMapping("/{no}")
  public String update(@PathVariable("no") Long no, @ModelAttribute Todo todo) {
    todoService.update(no, todo);
    todoService.changeStatus(no, todo.getStatus());
    return "redirect:/todos";
  }

  // 삭제 처리 : POST /todos/{no}/delete
  @PostMapping("/{no}/delete")
  public String delete(@PathVariable("no") Long no) {
    todoService.delete(no);
    return "redirect:/todos";
  }

}
