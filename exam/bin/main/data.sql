INSERT INTO todo (id, title, content, status, priority, due_date, created_at, updated_at) VALUES
  ('11111111-0000-0000-0000-000000000001', 'Spring Boot 프로젝트 생성', 'Spring Initializr로 exam 프로젝트 만들기', 'DONE',  1, DATEADD('DAY', -3, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('11111111-0000-0000-0000-000000000002', 'JPA 엔티티 작성',          'Todo 엔티티와 테이블 매핑',                'DOING', 1, DATEADD('DAY', -1, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('11111111-0000-0000-0000-000000000003', 'REST API 구현',            'Todo CRUD API 만들기',                    'TODO',  2, DATEADD('DAY',  2, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('11111111-0000-0000-0000-000000000004', '화면(MVC) 구현',           'Thymeleaf 목록/등록 화면',                 'TODO',  3, DATEADD('DAY',  5, CURRENT_DATE), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
