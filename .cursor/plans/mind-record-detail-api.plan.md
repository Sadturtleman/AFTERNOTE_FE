# MindRecordDetailScreen 리스트를 API mindRecords로 채우기 (수정)

## API 스펙: 기존 그대로 사용

**DTO/Entity 변경 없음.** [ReceivedApiDto.kt](app/src/main/java/com/kuit/afternote/feature/receiver/data/dto/ReceivedApiDto.kt)와 [ReceivedMindRecord](app/src/main/java/com/kuit/afternote/feature/receiver/domain/entity/ReceivedMindRecord.kt)를 그대로 사용한다.

- `ReceivedMindRecordResponseDto`: mindRecordId, sourceType, content, recordDate
- `ReceivedMindRecord`: mindRecordId, sourceType, content, recordDate
- [ReceivedMapper](app/src/main/java/com/kuit/afternote/feature/receiver/data/mapper/ReceivedMapper.kt) 기존 매핑 유지

---

## UI 매핑 (Entity → ExpandableRecordItem)

| ExpandableRecordItem | ReceivedMindRecord |
|---------------------|--------------------|
| date                | recordDate 포맷 ("yyyy년 M월 d일") |
| tags                | sourceType 또는 빈 문자열 |
| question            | sourceType 또는 빈 문자열 |
| content             | content |
| hasImage            | false (API에 필드 없음) |

---

## 나머지 구현 요약

1. **receiverId 전달**: [ReceiverMainRoute](app/src/main/java/com/kuit/afternote/feature/receiver/presentation/navgraph/ReceiverMainRoute.kt)에서 RECORD 탭에 `receiverId` 전달. MindRecordDetailScreen이 노출되는 경로(탭 내부 vs 별도 라우트)에 맞춰 `receiverId` 인자 추가.

2. **UiModel**: ExpandableRecordItem 한 건 분량 (date, tags, question, content, hasImage). Entity → UiModel 변환은 ViewModel 또는 Mapper 레이어에서 수행.

3. **MindRecordDetailViewModel**: receiverId 수신, GetReceivedMindRecordsUseCase(receiverId) 호출, `recordDate == selectedDate`(문자열 비교 또는 파싱)로 필터, UiModel 리스트로 매핑 후 UiState 노출. selectedDate 변경 시 재필터 또는 재요청.

4. **MindRecordDetailScreen**: ViewModel 연동, LazyColumn을 `uiState.mindRecordItems`로 채우고 하드코딩된 ExpandableRecordItem 2개 제거. Preview는 빈 리스트 또는 샘플 UiModel.
