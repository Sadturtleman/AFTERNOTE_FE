# 모든 afternote 목록

카테고리: Afternote
설명: category 없으면 전체
Method: GET
URL: /afternotes?category=SOCIAL?page=0&size=10
header 포함 여부: 포함 O
백엔드 관리자: 황규운
상태: 개발 진행 중

**Query parameter**

| **파라미터명** | **타입** | **필수여부** | **설명** | **예시** |
| --- | --- | --- | --- | --- |
| **`category`** | String | 선택 | 필터링할 카테고리 **(없으면 전체 조회)** | `SOCIAL`, `GALLERY`, 
`MUSIC` |
| **`page`** | Integer | 선택 | 페이지 번호 (0부터 시작) | `0` |
| **`size`** | Integer | 선택 | 한 번에 가져올 개수 (기본값 10) | `10` |

### Response

```jsx
{
    "status": 200,
    "code": 0,
    "message": "목록 조회에 성공하였습니다.",
    "data": {
        "content": [
            {
                "afternoteId": 10,
                "title": "인스타그램", 
                "category": "SOCIAL", 
                "createdAt": "2025-11-26T14:30:00"
            },
            {
                "afternoteId": 9,
                "title": "갤러리",
                "category": "GALLERY",
                "createdAt": "2025-11-26T10:15:00"
            },
            {
                "afternoteId": 8,
                "title": "업무용 노트",
                "category": "MUSIC",
                "createdAt": "2025-11-25T18:00:00"
            }
        ],
        "page": 0,       // 현재 페이지 번호
        "size": 10,      // 요청한 사이즈
        "hasNext": true  // 다음 페이지가 있는지 여부 (무한 스크롤 구현 시 필요)
    }
}
```

### Status

| status | response content |
| --- | --- |
| 200 |  |
| 400 |  |