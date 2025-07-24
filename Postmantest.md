📋 Postman 테스트 가이드

🔧 기본 설정

- Base URL: http://localhost:8080
- Content-Type: application/json (POST 요청시)

  ---
📝 게시글 API 테스트

1. 게시글 목록 조회

GET http://localhost:8080/board
응답 예시:
{
"success": true,
"message": "게시글 목록 조회가 완료되었습니다.",
"data": [
{
"idx": 1,
"title": "첫 번째 게시글",
"content": "게시글 내용",
"writerNm": "작성자1",
"regDate": "2024-01-01T10:00:00",
"delYn": "N",
"commentCount": 2,
"isNew": 0
}
],
"totalCount": 1
}

2. 게시글 등록

POST http://localhost:8080/board
Content-Type: application/json

{
"title": "테스트 게시글",
"content": "이것은 테스트 게시글입니다.",
"writerNm": "테스터"
}
응답 예시:
{
"success": true,
"message": "게시글이 성공적으로 등록되었습니다.",
"data": null,
"totalCount": null
}

3. 게시글 상세 조회 (댓글 포함)

GET http://localhost:8080/board/1
응답 예시:
{
"success": true,
"message": "게시글 상세 조회가 완료되었습니다.",
"data": {
"board": {
"idx": 1,
"title": "첫 번째 게시글",
"content": "게시글 내용",
"writerNm": "작성자1",
"regDate": "2024-01-01T10:00:00",
"delYn": "N"
},
"comments": [
{
"idx": 1,
"boardIdx": 1,
"comment": "첫 번째 댓글",
"writerName": "댓글작성자1",
"regDate": "2024-01-01T11:00:00",
"delYn": "N"
}
],
"commentCount": 1
}
}

  ---
💬 댓글 API 테스트

4. 댓글 등록

POST http://localhost:8080/comment
Content-Type: application/json

{
"boardIdx": 1,
"comment": "이것은 테스트 댓글입니다.",
"writerName": "댓글테스터"
}

5. 특정 게시글의 댓글 목록 조회

GET http://localhost:8080/comment/board/1

6. 전체 댓글 목록 조회

GET http://localhost:8080/comment

7. 댓글 삭제

DELETE http://localhost:8080/comment/1

  ---
🧪 테스트 시나리오

📋 기본 기능 테스트 순서

1. 게시글 목록 조회 (빈 목록 확인)
2. 게시글 등록 (여러 개 등록)
3. 게시글 목록 조회 (등록된 게시글 확인)
4. 게시글 상세 조회 (댓글 없는 상태)
5. 댓글 등록 (해당 게시글에)
6. 게시글 상세 조회 (댓글 포함 확인)
7. 댓글 목록 조회 (전체 및 특정 게시글)
8. 댓글 삭제
9. 삭제 후 조회 (삭제 확인)

🚨 에러 케이스 테스트

유효성 검증 에러 테스트

1. 게시글 등록 - 필수값 누락
   POST http://localhost:8080/board
   {
   "title": "",
   "content": "내용만 있음"
   }
   예상 응답: 400 Bad Request

2. 댓글 등록 - 필수값 누락
   POST http://localhost:8080/comment
   {
   "boardIdx": 1,
   "comment": ""
   }

3. 글자수 제한 초과
   POST http://localhost:8080/board
   {
   "title": "a".repeat(101),  // 100자 초과
   "content": "내용",
   "writerNm": "작성자"
   }

존재하지 않는 데이터 테스트

4. 존재하지 않는 게시글 조회
   GET http://localhost:8080/board/999
   예상 응답: 400 Bad Request - "게시글을 찾을 수 없습니다."

5. 존재하지 않는 게시글에 댓글 작성
   POST http://localhost:8080/comment
   {
   "boardIdx": 999,
   "comment": "존재하지 않는 게시글에 댓글",
   "writerName": "작성자"
   }
   예상 응답: 400 Bad Request - 외래키 제약조건 오류

  ---
📊 Postman Collection 예시

Collection 구성

📁 Board API Test
├── 📁 Boards
│   ├── GET - 게시글 목록 조회
│   ├── POST - 게시글 등록
│   ├── GET - 게시글 상세 조회 (성공)
│   └── GET - 게시글 상세 조회 (실패 - 존재하지 않음)
├── 📁 Comments
│   ├── POST - 댓글 등록
│   ├── GET - 특정 게시글 댓글 조회
│   ├── GET - 전체 댓글 조회
│   └── DELETE - 댓글 삭제
└── 📁 Error Cases
├── POST - 게시글 등록 (유효성 오류)
├── POST - 댓글 등록 (유효성 오류)
└── POST - 댓글 등록 (외래키 오류)

🔍 응답 확인 포인트

1. HTTP 상태 코드: 200 (성공), 400 (클라이언트 오류), 500 (서버 오류)
2. 응답 구조: success, message, data 필드 확인
3. 데이터 타입: 날짜, 숫자 등이 올바른 형식인지 확인
4. 비즈니스 로직: commentCount가 실제 댓글 수와 일치하는지 등

이렇게 체계적으로 테스트하시면 SOLID 원칙이 적용된 리팩토링된 코드가 올바르게 동작하는지 확인할 수 있습니다!