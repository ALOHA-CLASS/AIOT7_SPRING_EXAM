package com.aloha.exam.api;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.exam.domain.Todo;
import com.aloha.exam.domain.TodoStatus;
import com.aloha.exam.service.inter.TodoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST API 컨트롤러 - JSON 데이터를 반환한다.
 */
@Slf4j
/* [문제 5-①] JSON 데이터를 반환하는 REST 컨트롤러로 등록하는 어노테이션을 작성하시오. */
/* [문제 5-②] 이 컨트롤러의 공통 요청 경로를 "/api/todos" 로 지정하는 어노테이션을 작성하시오. */
@RequiredArgsConstructor
public class TodoApi {

  private final TodoService todoService;

  // 목록 조회 : GET /api/todos?status=TODO
  @GetMapping
  public ResponseEntity<List<Todo>> list(@RequestParam(value = "status", required = false) TodoStatus status) {
    return ResponseEntity.ok(todoService.list(status));
  }

  // 제목 검색 : GET /api/todos/search?keyword=API
  @GetMapping("/search")
  public ResponseEntity<List<Todo>> search(@RequestParam("keyword") String keyword) {
    return ResponseEntity.ok(todoService.search(keyword));
  }

  // 마감이 지난 미완료 목록 : GET /api/todos/overdue
  @GetMapping("/overdue")
  public ResponseEntity<List<Todo>> overdue() {
    return ResponseEntity.ok(todoService.overdue());
  }

  // 상태별 개수 : GET /api/todos/count?status=TODO
  @GetMapping("/count")
  public ResponseEntity<Long> count(@RequestParam("status") TodoStatus status) {
    return ResponseEntity.ok(todoService.count(status));
  }

  // 단건 조회 : GET /api/todos/{no}
  @GetMapping("/{no}")
  public ResponseEntity<Todo> get(@PathVariable("no") Long no) {
    return ResponseEntity.ok(todoService.get(no));
  }

  // 등록 : POST /api/todos (JSON) -> 201 Created
  /* [문제 5-③] POST 방식 요청을 처리하는 어노테이션을 작성하시오. */
  public ResponseEntity<Todo> create(/* [문제 5-④] 요청 본문(JSON)을 Todo 객체로 변환하는 어노테이션을 작성하시오. */ Todo todo) {
    Todo created = todoService.create(todo);
    /* [문제 5-⑤] 상태코드 201(Created)과 created 를 본문에 담아 반환하도록 작성하시오. */
    return null;
  }

  // 수정 : PUT /api/todos/{no} (JSON) -> 200 OK
  @PutMapping("/{no}")
  public ResponseEntity<Todo> update(@PathVariable("no") Long no, @RequestBody Todo todo) {
    return ResponseEntity.ok(todoService.update(no, todo));
  }

  // 상태 변경 : PATCH /api/todos/{no}/status?status=DONE -> 200 OK
  /* [문제 5-⑥] PATCH 방식, 경로 "/{no}/status" 요청을 처리하는 어노테이션을 작성하시오. */
  public ResponseEntity<Todo> changeStatus(/* [문제 5-⑦] 경로의 {no} 값을 받는 어노테이션을 작성하시오. */ Long no, @RequestParam("status") TodoStatus status) {
    return ResponseEntity.ok(todoService.changeStatus(no, status));
  }

  // 삭제 : DELETE /api/todos/{no} -> 204 No Content
  @DeleteMapping("/{no}")
  public ResponseEntity<Void> delete(@PathVariable("no") Long no) {
    todoService.delete(no);
    /* [문제 5-⑧] 상태코드 204(No Content)를 반환하도록 작성하시오. */
    return null;
  }

  // 존재하지 않는 할일 -> 404 Not Found
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> notFound(NoSuchElementException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

}
