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

### 9. 댓글 삭제 (🔒 인증 필요 - 본인 작성 댓글만)
```
DELETE http://localhost:8080/comment/1
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

**성공 응답 (본인 댓글):**
```json
{
    "success": true,
    "message": "댓글이 성공적으로 삭제되었습니다.",
    "data": null
}
```

**실패 응답 (다른 사용자 댓글):**
```json
{
    "success": false,
    "message": "본인이 작성한 댓글만 삭제할 수 있습니다.",
    "data": null
}
```

### 10. 게시글 수정 (🔒 인증 필요 - 본인 작성 게시글만)
```
PUT http://localhost:8080/board/1
Authorization: Bearer YOUR_JWT_TOKEN_HERE
Content-Type: application/json

{
    "title": "수정된 게시글 제목",
    "content": "수정된 게시글 내용입니다."
}
```

**성공 응답 (본인 게시글):**
```json
{
    "success": true,
    "message": "게시글이 성공적으로 수정되었습니다.",
    "data": null
}
```

**실패 응답 (다른 사용자 게시글):**
```json
{
    "success": false,
    "message": "본인이 작성한 게시글만 수정할 수 있습니다.",
    "data": null
}
```

### 11. 게시글 삭제 (🔒 인증 필요 - 본인 작성 게시글만)
```
DELETE http://localhost:8080/board/1
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

**성공 응답 (본인 게시글):**
```json
{
    "success": true,
    "message": "게시글이 성공적으로 삭제되었습니다.",
    "data": null
}
```

**실패 응답 (다른 사용자 게시글):**
```json
{
    "success": false,
    "message": "본인이 작성한 게시글만 삭제할 수 있습니다.",
    "data": null
}
```

---

## 🧪 테스트 시나리오

### 📋 기본 인증 플로우 테스트

1. **로그인** → JWT 토큰 발급받기
2. **토큰 검증** → 발급받은 토큰이 유효한지 확인
3. **게시글 등록** → 인증된 사용자로 게시글 작성
4. **댓글 등록** → 인증된 사용자로 댓글 작성
5. **목록 조회** → 작성자 정보가 포함되어 조회되는지 확인
6. **본인 글 수정** → 작성자 권한으로 게시글 수정 성공
7. **본인 글/댓글 삭제** → 작성자 권한으로 삭제 성공
8. **타인 글 수정/삭제 시도** → 권한 없음으로 403 에러 발생

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

#### 다른 사용자의 게시글 수정/삭제 시도 (권한 없음)
```
PUT http://localhost:8080/board/1
Authorization: Bearer USER2_JWT_TOKEN_HERE
Content-Type: application/json

{
    "title": "해킹 시도",
    "content": "다른 사용자의 글을 수정하려 합니다."
}
```
**예상 응답:** 403 Forbidden

```
DELETE http://localhost:8080/board/1
Authorization: Bearer USER2_JWT_TOKEN_HERE
```
**예상 응답:** 403 Forbidden

```
DELETE http://localhost:8080/comment/1  
Authorization: Bearer USER2_JWT_TOKEN_HERE
```
**예상 응답:** 403 Forbidden

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
│   ├── PUT - 게시글 수정 (본인만)
│   ├── DELETE - 게시글 삭제 (본인만)
│   └── POST - 게시글 등록 (인증 없음 - 실패)
├── 📁 Comments (Protected)
│   ├── POST - 댓글 등록 (인증)
│   ├── GET - 특정 게시글 댓글 조회
│   ├── GET - 전체 댓글 조회
│   ├── DELETE - 댓글 삭제 (본인만)
│   └── POST - 댓글 등록 (인증 없음 - 실패)
├── 📁 Authorization Tests
│   ├── PUT - 타인 게시글 수정 시도 (403)
│   ├── DELETE - 타인 게시글 삭제 시도 (403)
│   └── DELETE - 타인 댓글 삭제 시도 (403)
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

### 4. 권한 확인 (신규 추가)
- 본인이 작성한 게시글만 수정/삭제 가능한지
- 본인이 작성한 댓글만 삭제 가능한지
- 다른 사용자의 게시글 수정/삭제 시도시 403 에러 발생하는지
- 논리 삭제가 정상적으로 동작하는지 (`delYn = 'Y'`)

---

## 🧪 권한 테스트 시나리오 예시

### 1. 두 명의 사용자로 테스트
```
1. user1으로 로그인 → 게시글/댓글 작성
2. user2로 로그인 → user1의 게시글 수정/삭제 시도 → 403 에러 확인
3. user1으로 다시 로그인 → 본인 게시글 수정/삭제 → 성공 확인
```

### 2. 수정/삭제 확인 방법
```
1. 게시글/댓글 작성 후 목록에서 확인
2. 수정 API 호출 → 목록에서 변경된 내용 확인
3. 삭제 API 호출 → 목록 조회 시 해당 항목이 보이지 않는지 확인
```

---

이제 완전한 권한 관리 시스템이 구축되었습니다! 본인이 작성한 게시글은 수정/삭제가 가능하고, 댓글은 삭제만 가능한 안전한 게시판 API입니다. 🎉