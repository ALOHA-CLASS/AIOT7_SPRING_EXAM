package com.aloha.exam.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
/* [문제 1-①] 이 클래스를 JPA 엔티티로 선언하는 어노테이션을 작성하시오. */
/* [문제 1-②] 이 엔티티가 매핑될 테이블 이름(todo)을 지정하는 어노테이션을 작성하시오. */
public class Todo extends Base {

  /* [문제 1-③] 제목 : NULL 불가, 최대 길이 100 이 되도록 컬럼을 설정하는 어노테이션을 작성하시오. */
  private String title;               // 제목

  @Column(length = 1000)
  private String content;             // 내용

  /* [문제 1-④] enum 이름(예: "TODO")이 문자열 그대로 저장되도록 설정하는 어노테이션을 작성하시오. */
  @Column(nullable = false, length = 20)
  private TodoStatus status = TodoStatus.TODO;   // 상태

  private Integer priority;           // 우선순위 (1:높음 ~ 3:낮음)

  private LocalDate dueDate;          // 마감일

}
