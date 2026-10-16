package com.aloha.exam.service.inter;

import java.util.List;

import com.aloha.exam.domain.Todo;
import com.aloha.exam.domain.TodoStatus;

public interface TodoService {

  // 목록 조회 (status가 null이면 전체)
  List<Todo> list(TodoStatus status);

  // 제목 검색 (우선순위 오름차순)
  List<Todo> search(String keyword);

  // 마감이 지난 미완료 할일 목록
  List<Todo> overdue();

  // 상태별 개수
  long count(TodoStatus status);

  // 단건 조회 (없으면 NoSuchElementException)
  Todo get(Long no);

  // 등록
  Todo create(Todo todo);

  // 수정 (제목, 내용, 우선순위, 마감일)
  Todo update(Long no, Todo form);

  // 상태 변경
  Todo changeStatus(Long no, TodoStatus status);

  // 삭제
  void delete(Long no);

}
