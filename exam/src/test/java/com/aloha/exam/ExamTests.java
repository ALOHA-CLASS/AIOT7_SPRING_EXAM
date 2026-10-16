package com.aloha.exam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 시험 답안 확인용 테스트 (수정하지 않는 파일)
 * 실행 : gradlew test
 * 모든 문제를 완성하면 전체 테스트가 통과한다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ExamTests {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  JdbcTemplate jdbc;

  // ===== 문제 1 : 엔티티 / 테이블 정의 =====
  @Test
  @DisplayName("문제1 - todo 테이블 컬럼 정의")
  void q1_tableDefinition() {
    List<Map<String, Object>> cols = jdbc.queryForList(
        "SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE "
      + "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'TODO'");
    Map<String, Map<String, Object>> byName = new java.util.HashMap<>();
    cols.forEach(c -> byName.put((String) c.get("COLUMN_NAME"), c));

    assertThat(byName).containsKeys("NO", "ID", "TITLE", "CONTENT", "STATUS", "PRIORITY", "DUE_DATE");
    assertThat(byName.get("TITLE").get("IS_NULLABLE")).isEqualTo("NO");
    assertThat(((Number) byName.get("TITLE").get("CHARACTER_MAXIMUM_LENGTH")).intValue()).isEqualTo(100);
    assertThat(byName.get("STATUS").get("IS_NULLABLE")).isEqualTo("NO");
    assertThat(byName.get("ID").get("IS_NULLABLE")).isEqualTo("NO");
    assertThat(byName.get("PRIORITY").get("IS_NULLABLE")).isEqualTo("YES");
    assertThat(byName.get("CONTENT").get("IS_NULLABLE")).isEqualTo("YES");
  }

  // ===== 문제 2 : Repository (Spring Data JPA) =====
  @Test
  @DisplayName("문제2 - 상태별 조회 / 개수")
  void q2_findByStatusAndCount() throws Exception {
    mockMvc.perform(get("/api/todos").param("status", "TODO"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(2)))
        .andExpect(jsonPath("$[?(@.status != 'TODO')]").isEmpty());
    mockMvc.perform(get("/api/todos/count").param("status", "DONE"))
        .andExpect(status().isOk())
        .andExpect(content().string("1"));
  }

  @Test
  @DisplayName("문제2 - 제목 검색 (우선순위 오름차순)")
  void q2_search() throws Exception {
    mockMvc.perform(get("/api/todos/search").param("keyword", "구현"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].priority").value(2))
        .andExpect(jsonPath("$[1].priority").value(3));
  }

  @Test
  @DisplayName("문제2 - JPQL 마감 지난 미완료 목록")
  void q2_overdue() throws Exception {
    mockMvc.perform(get("/api/todos/overdue"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].title").value("JPA 엔티티 작성"));
  }

  // ===== 문제 3 : Service =====
  @Test
  @DisplayName("문제3 - 등록 시 기본 상태 TODO, 수정(변경 감지), 삭제")
  void q3_service() throws Exception {
    mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"서비스 테스트\",\"priority\":2}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("TODO"));

    Long no = jdbc.queryForObject("SELECT NO FROM TODO WHERE TITLE = '서비스 테스트'", Long.class);

    mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/todos/" + no)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"서비스 수정\",\"priority\":1}"))
        .andExpect(status().isOk());
    assertThat(jdbc.queryForObject("SELECT TITLE FROM TODO WHERE NO = ?", String.class, no)).isEqualTo("서비스 수정");

    mockMvc.perform(delete("/api/todos/" + no)).andExpect(status().isNoContent());
    mockMvc.perform(get("/api/todos/" + no)).andExpect(status().isNotFound());
  }

  // ===== 문제 4 : MVC Controller =====
  @Test
  @DisplayName("문제4 - 목록 화면 (Model / View)")
  void q4_list() throws Exception {
    mockMvc.perform(get("/todos"))
        .andExpect(status().isOk())
        .andExpect(view().name("todo/list"))
        .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.model().attributeExists("todos"));
  }

  @Test
  @DisplayName("문제4 - 등록 처리 후 목록으로 redirect")
  void q4_create() throws Exception {
    mockMvc.perform(post("/todos").param("title", "폼 등록").param("priority", "2"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/todos"));
    assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM TODO WHERE TITLE = '폼 등록'", Integer.class)).isEqualTo(1);
  }

  @Test
  @DisplayName("문제4 - 수정 화면 (PathVariable)")
  void q4_edit() throws Exception {
    Long no = jdbc.queryForObject("SELECT NO FROM TODO WHERE TITLE = 'REST API 구현'", Long.class);
    mockMvc.perform(get("/todos/" + no + "/edit"))
        .andExpect(status().isOk())
        .andExpect(view().name("todo/form"));
  }

  // ===== 문제 5 : REST API =====
  @Test
  @DisplayName("문제5 - POST 201 / PATCH / DELETE 204 / 404")
  void q5_rest() throws Exception {
    mockMvc.perform(get("/api/todos")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").isNumber());

    mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"REST 테스트\",\"priority\":1}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("REST 테스트"));

    Long no = jdbc.queryForObject("SELECT NO FROM TODO WHERE TITLE = 'REST 테스트'", Long.class);

    mockMvc.perform(patch("/api/todos/" + no + "/status").param("status", "DONE"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("DONE"));

    mockMvc.perform(delete("/api/todos/" + no)).andExpect(status().isNoContent());
    mockMvc.perform(get("/api/todos/" + no)).andExpect(status().isNotFound());
  }

}
