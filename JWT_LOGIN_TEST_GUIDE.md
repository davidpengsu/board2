# 🔐 JWT 로그인 기능 적용된 게시판 API 테스트 가이드

### 1. 회원가입 먼저 하기
```
POST http://localhost:8080/auth/signup
Content-Type: application/json

{
"userId": "newuser",
"password": "test123",
"username": "새사용자",
"email": "newuser@example.com"
}
```

---

## 🔑 인증 API 테스트

### 1. 로그인
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
    "userId": "newuser",
    "password": "test123"
}
```

**성공 응답:**
```json
{
    "success": true,
    "message": "로그인이 성공적으로 완료되었습니다.",
    "data": {
        "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
        "tokenType": "Bearer",
        "expiresIn": 86400,
        "userInfo": {
            "userId": "user1",
            "username": "사용자1",
            "email": "user1@example.com",
            "role": "USER"
        }
    }
}
```

### 2. 토큰 검증 (개발/테스트용)
```
GET http://localhost:8080/auth/validate
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

---

## 📝 게시글 API 테스트 (🔒 인증 필요)

### 3. 게시글 목록 조회 (인증 필요 없음)
```
GET http://localhost:8080/board
```

### 4. 게시글 등록 (🔒 인증 필요)
```
POST http://localhost:8080/board
Authorization: Bearer YOUR_JWT_TOKEN_HERE
Content-Type: application/json

{
    "title": "로그인 사용자가 작성한 게시글",
    "content": "JWT 토큰으로 인증된 사용자가 작성한 내용입니다."
}
```

**참고:** `writerId`는 JWT 토큰에서 자동으로 추출되어 설정됩니다.

### 5. 게시글 상세 조회 (인증 필요 없음)
```
GET http://localhost:8080/board/1
```

---

## 💬 댓글 API 테스트 (🔒 인증 필요)

### 6. 댓글 등록 (🔒 인증 필요)
```
POST http://localhost:8080/comment
Authorization: Bearer YOUR_JWT_TOKEN_HERE
Content-Type: application/json

{
    "boardIdx": 1,
    "comment": "로그인 사용자가 작성한 댓글입니다."
}
```

### 7. 특정 게시글의 댓글 목록 조회 (인증 필요 없음)
```
GET http://localhost:8080/comment/board/1
```

### 8. 전체 댓글 목록 조회 (인증 필요 없음)
```
GET http://localhost:8080/comment
```

### 9. 댓글 삭제 (🔒 인증 필요)
```
DELETE http://localhost:8080/comment/1
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

---

## 🧪 테스트 시나리오

### 📋 기본 인증 플로우 테스트

1. **로그인** → JWT 토큰 발급받기
2. **토큰 검증** → 발급받은 토큰이 유효한지 확인
3. **게시글 등록** → 인증된 사용자로 게시글 작성
4. **댓글 등록** → 인증된 사용자로 댓글 작성
5. **목록 조회** → 작성자 정보가 포함되어 조회되는지 확인

### 🚨 에러 케이스 테스트

#### 인증 없이 보호된 리소스 접근
```
POST http://localhost:8080/board
Content-Type: application/json

{
    "title": "인증 없이 작성 시도",
    "content": "이 요청은 실패해야 합니다.",
    "writerNm": "익명"
}
```
**예상 응답:** 401 Unauthorized

#### 잘못된 토큰으로 접근
```
POST http://localhost:8080/board
Authorization: Bearer invalid_token_here
Content-Type: application/json

{
    "title": "잘못된 토큰으로 작성 시도",
    "content": "이 요청은 실패해야 합니다.",
    "writerNm": "해커"
}
```
**예상 응답:** 401 Unauthorized

#### 로그인 실패
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
    "userId": "user1",
    "password": "wrongpassword"
}
```
**예상 응답:** 400 Bad Request

---

## 📊 Postman Collection 구성

```
📁 JWT 게시판 API 테스트
├── 📁 Authentication
│   ├── POST - 로그인 (user1)
│   ├── POST - 로그인 (admin) 
│   ├── GET - 토큰 검증
│   └── POST - 로그인 실패 테스트
├── 📁 Boards (Protected)
│   ├── GET - 게시글 목록 조회
│   ├── POST - 게시글 등록 (인증)
│   ├── GET - 게시글 상세 조회
│   └── POST - 게시글 등록 (인증 없음 - 실패)
├── 📁 Comments (Protected)
│   ├── POST - 댓글 등록 (인증)
│   ├── GET - 특정 게시글 댓글 조회
│   ├── GET - 전체 댓글 조회
│   ├── DELETE - 댓글 삭제 (인증)
│   └── POST - 댓글 등록 (인증 없음 - 실패)
```

---

## ⚙️ Postman 환경 변수 설정

Postman에서 환경 변수를 설정하면 테스트가 더 편리합니다:

- `baseUrl`: `http://localhost:8080`
- `accessToken`: 로그인 후 받은 JWT 토큰
- `userId`: `user1`
- `password`: `password123!`

### 로그인 응답에서 토큰 자동 추출 스크립트:
```javascript
// 로그인 API의 Tests 탭에 추가
if (pm.response.code === 200) {
    const responseJson = pm.response.json();
    if (responseJson.success && responseJson.data.accessToken) {
        pm.environment.set("accessToken", responseJson.data.accessToken);
        console.log("토큰이 환경변수에 저장되었습니다:", responseJson.data.accessToken);
    }
}
```

### Authorization 헤더 자동 설정:
인증이 필요한 API에서 `Authorization` 탭에서:
- Type: `Bearer Token`
- Token: `{{accessToken}}`

---

## 🔍 확인 포인트

### 1. 응답 데이터 확인
- 게시글/댓글에 `writerId` 필드가 포함되어 있는지
- 로그인한 사용자의 ID가 올바르게 설정되었는지

### 2. 보안 확인
- 인증 없이 보호된 API 호출시 401 에러 발생
- 잘못된 토큰으로 호출시 401 에러 발생
- 토큰 만료시 401 에러 발생

### 3. 기능 확인
- JWT 토큰에서 사용자 정보가 올바르게 추출되는지
- 인증된 사용자 정보가 게시글/댓글에 자동으로 설정되는지

---

이제 로그인한 사용자만 게시글과 댓글을 작성할 수 있고, 작성자 정보가 자동으로 연동되는 완전한 JWT 인증 시스템이 구축되었습니다! 🎉