package com.aloha.exam.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aloha.exam.domain.Todo;
import com.aloha.exam.domain.TodoStatus;

/* [문제 2-①] Todo 엔티티(PK 타입 Long)의 기본 CRUD 메소드를 제공하는 JpaRepository를 상속하도록 선언을 완성하시오. */
public interface TodoRepository {

  /* [문제 2-②] 상태(status)가 일치하는 할일 목록을 조회하는 쿼리 메소드를 작성하시오. */

  /* [문제 2-③] 제목(title)에 키워드가 포함된 할일 목록을 우선순위(priority) 오름차순으로 조회하는 쿼리 메소드를 작성하시오. */

  /* [문제 2-④] 상태(status)별 할일 개수를 반환하는 쿼리 메소드를 작성하시오. (반환 타입: long) */

  /*
   * [문제 2-⑤] [문제 2-⑥]
   * 마감일(dueDate)이 today 이전이면서 상태(status)가 파라미터 status와 같지 않은 할일 목록을
   * 마감일 오름차순으로 조회하는 JPQL 메소드 findOverdue 를 작성하시오.
   * (조회 결과 List<Todo>, 파라미터 : LocalDate today, TodoStatus status)
   */

}
