package com.aloha.exam.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aloha.exam.domain.Todo;
import com.aloha.exam.domain.TodoStatus;
import com.aloha.exam.repository.TodoRepository;
import com.aloha.exam.service.inter.TodoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/* [문제 3-①] 이 클래스를 서비스 빈(Bean)으로 등록하는 어노테이션을 작성하시오. */
/* [문제 3-②] 클래스 전체를 읽기 전용 트랜잭션으로 설정하는 어노테이션을 작성하시오. */
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

  private final TodoRepository todoRepository;    // 생성자 주입

  @Override
  public List<Todo> list(TodoStatus status) {
    if (status == null) {
      return todoRepository.findAll();
    }
    return todoRepository.findByStatus(status);
  }

  @Override
  public List<Todo> search(String keyword) {
    return todoRepository.findByTitleContainingOrderByPriorityAsc(keyword);
  }

  @Override
  public List<Todo> overdue() {
    return todoRepository.findOverdue(LocalDate.now(), TodoStatus.DONE);
  }

  @Override
  public long count(TodoStatus status) {
    return todoRepository.countByStatus(status);
  }

  @Override
  public Todo get(Long no) {
    /* [문제 3-③] no로 조회하고, 없으면 NoSuchElementException("Todo not found: " + no) 을 던지도록 작성하시오. */
    return null;
  }

  @Override
  /* [문제 3-④] 쓰기 작업이므로 읽기 전용을 해제하는 트랜잭션 어노테이션을 작성하시오. */
  public Todo create(Todo todo) {
    if (todo.getStatus() == null) {
      todo.setStatus(TodoStatus.TODO);
    }
    /* [문제 3-⑤] todo 를 저장하고, 저장된 결과를 반환하도록 작성하시오. */
    return null;
  }

  @Override
  @Transactional
  public Todo update(Long no, Todo form) {
    Todo todo = get(no);
    /* [문제 3-⑥] form 의 제목, 내용, 우선순위, 마감일을 todo 에 반영하시오.
       (save() 를 호출하지 않아도 변경 감지(Dirty Checking)로 UPDATE 가 실행된다) */
    return todo;
  }

  @Override
  @Transactional
  public Todo changeStatus(Long no, TodoStatus status) {
    Todo todo = get(no);
    todo.setStatus(status);
    return todo;
  }

  @Override
  @Transactional
  public void delete(Long no) {
    todoRepository.delete(get(no));
  }

}
