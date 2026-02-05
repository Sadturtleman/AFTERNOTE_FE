# afternote 상세 목록

카테고리: Afternote
설명: 상세 목록
Method: GET
URL: /afternotes/{afternote_id}
header 포함 여부: 포함 O
백엔드 관리자: 황규운
상태: 개발 진행 중

**Path Variable**

```java
afternote_id : int
```

### Response

```jsx
{
  "status": 200,
  "code": 0,
  "message": "상세 조회에 성공하였습니다.",
  "data": {
    // ==========================================
    // [1] 공통 영역 (모든 카테고리 필수)
    // ==========================================
    "afternoteId": 10,
    "category": "SOCIAL", // "GALLERY", "PLAYLIST"
    "title": "인스타그램", 
    "createdAt": "2025-11-26T14:30:00",
    "updatedAt": "2025-11-26T14:30:00",

    // ==========================================
    // [2] 카테고리별 전용 영역 (나머지는 null)
    // ==========================================
    
    // (A) SOCIAL 일 때만 값 있음
    "credentials": { 
        "id": "아이디", 
        "password": "qwerty123" 
    },

    // (B) GALLERY 일 때만 값 있음 (현재는 SOCIAL이라 null)
    "receivers": null,
    /* "receivers": [
        { "name": "황규운", "relation": "친구", "phone": "010-0000-0000" }
    ] 
    */
    
    // (C)SOCIAL 혹은 GALLERY 일 때만
    
     // 이 메모에 대해 선택한 처리 방식 (라디오 버튼 값)
    // SOCIAL: 'MEMORIAL', 'DELETE', 'TRANSFER'
    // GALLERY: 'TRANSFER', 'ADDITIONAL'
    "processMethod": "MEMORIAL", 
    
     // 체크리스트 (사용자가 선택/입력한 할 일 목록)
    "actions": [                
        "게시물 내리기",
        "추모 게시물 올리기",
        "내 흑역사 폴더 영구 삭제" 
    ], 
      
    // 남기신 말씀 (텍스트 에어리어)
    "leaveMessage": "사망 후 추모 계정으로 전환해줘. 우리 가족 사진은 지키고 싶어.",

    
    // (D) PLAYLIST 일 때만 값 있음 (현재는 SOCIAL이라 null)
    "playlist": null
    /*
    "playlist": {
        "profilePhoto": "http://...",
        "atmosphere": "차분하고 조용하게 보내주세요.", 
        "songs": [
            { "id": 1, "title": "노래제목", "artist": "가수", "coverUrl": "..." }
        ],
        "memorialVideo": {
            "videoUrl": "...",
            "thumbnailUrl": "..."
        }
    }
    */
  }
}
```

### Status

| status | response content |
| --- | --- |
| 200 |  |
| 400 |  |