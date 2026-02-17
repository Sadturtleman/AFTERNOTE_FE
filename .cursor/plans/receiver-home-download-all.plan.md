---
name: ""
overview: ""
todos: []
isProject: false
---

# Receiver HOME: 탭 이동 + 모든 기록 내려받기 (구현 계획)

## 목표

1. **HOME 탭 ContentSection 버튼:** 마음의 기록 / 타임레터 / 애프터노트 섹션의 버튼을 누르면 해당 탭(화면)으로 전환.
2. **모든 기록 내려받기:** 다이얼로그 "예" 선택 시 receiverId로 타임레터·마인드레코드·애프터노트 세 API를 호출하여 그 발신자의 기록을 모두 받음.

## 전제

- 수신자별로 진입하면 **마스터키로 발신자가 1:1 매칭**되어 있음. receiverId로 API를 호출하면 **그 발신자의 내용만** 내려옴. senderId 필드·필터링 없이 receiverId만 사용.
- **DTO는 스웨거 명세 유지.** 변경 없음.

---

## Part 1: ContentSection 버튼 → 탭 전환

### 1.1 ContentSection

- **파일:** [ContentSection.kt](app/src/main/java/com/kuit/afternote/feature/receiver/presentation/component/ContentSection.kt)
- **변경:** 파라미터 `onButtonClick: () -> Unit = {}` 추가. `Button(onClick = { })` → `Button(onClick = onButtonClick)`.

### 1.2 ReceiverAfternoteScreen

- **파일:** [ReceiverAfternoteScreen.kt](app/src/main/java/com/kuit/afternote/feature/receiver/presentation/screen/ReceiverAfternoteScreen.kt)
- **변경:**
  - 파라미터 추가 (기본값 `{}`로 Preview 호환):  
  `onNavigateToRecord`, `onNavigateToTimeLetter`, `onNavigateToAfternote` (각 `() -> Unit`).
  - 세 개 ContentSection에 각각 `onButtonClick = onNavigateToRecord` / `onNavigateToTimeLetter` / `onNavigateToAfternote` 전달.

### 1.3 ReceiverMainRoute

- **파일:** [ReceiverMainRoute.kt](app/src/main/java/com/kuit/afternote/feature/receiver/presentation/navgraph/ReceiverMainRoute.kt)
- **변경:**  
`ReceiverAfterNoteScreen(showBottomBar = false)` 호출을 다음으로 교체:
  - `onNavigateToRecord = { selectedBottomNavItem = BottomNavItem.RECORD }`
  - `onNavigateToTimeLetter = { selectedBottomNavItem = BottomNavItem.TIME_LETTER }`
  - `onNavigateToAfternote = { selectedBottomNavItem = BottomNavItem.AFTERNOTE }`

---

## Part 2: receiverId 전달

- **ReceiverMainRoute:** 이미 `receiverId: String` 보유.  
`ReceiverAfterNoteScreen(..., receiverId = receiverId)` 인자 추가.
- **ReceiverAfternoteScreen:** `receiverId: String = "1"` 파라미터 추가 (Preview용 기본값).

---

## Part 3: 모든 기록 내려받기 (Domain · Data)

### 3.1 도메인 엔티티: ReceivedMindRecord

- **파일 (신규):** `feature/receiver/domain/entity/ReceivedMindRecord.kt`
- **내용:** [ReceivedMindRecordResponseDto](app/src/main/java/com/kuit/afternote/feature/receiver/data/dto/ReceivedApiDto.kt) 필드에 맞춘 data class.  
  - `mindRecordId: Long`, `sourceType: String?`, `content: String?`, `recordDate: String?`

### 3.2 도메인 Repository: ReceivedMindRecordRepository

- **파일 (신규):** `feature/receiver/domain/repository/iface/ReceivedMindRecordRepository.kt`
- **내용:**  
`suspend fun getReceivedMindRecords(receiverId: Long): Result<List<ReceivedMindRecord>>`

### 3.3 Data: ReceivedMindRecordRepositoryImpl + Mapper

- **파일 (신규):** `feature/receiver/data/repository/impl/ReceivedMindRecordRepositoryImpl.kt`
  - [ReceivedRepository](app/src/main/java/com/kuit/afternote/feature/receiver/data/repository/iface/ReceivedRepository.kt)의 `getReceivedMindRecords(receiverId)` 호출 후, DTO 리스트를 Mapper로 엔티티로 변환해 반환. (기존 [ReceivedTimeLetterRepositoryImpl](app/src/main/java/com/kuit/afternote/feature/receiver/data/repository/impl/ReceivedTimeLetterRepositoryImpl.kt) 패턴과 동일.)
- **ReceivedMapper:** `toReceivedMindRecord(dto: ReceivedMindRecordResponseDto): ReceivedMindRecord` 함수 추가.

### 3.4 UseCase: GetReceivedMindRecordsUseCase

- **파일 (신규):** `feature/receiver/domain/usecase/GetReceivedMindRecordsUseCase.kt`
- **내용:** [GetReceivedTimeLettersUseCase](app/src/main/java/com/kuit/afternote/feature/receiver/domain/usecase/GetReceivedTimeLettersUseCase.kt)와 동일 패턴.  
`ReceivedMindRecordRepository.getReceivedMindRecords(receiverId)` 호출 후 그대로 반환.

### 3.5 UseCase: DownloadAllReceivedUseCase

- **파일 (신규):** `feature/receiver/domain/usecase/DownloadAllReceivedUseCase.kt`
- **의존성:** GetReceivedTimeLettersUseCase, GetReceivedMindRecordsUseCase, GetReceivedAfterNotesUseCase 주입.
- **반환 타입:** 세 결과를 묶은 data class (예: `data class DownloadAllResult(val timeLetters: List<ReceivedTimeLetter>, val mindRecords: List<ReceivedMindRecord>, val afternotes: List<ReceivedAfternote>)`).
- **동작:** `invoke(receiverId: Long)`: 세 UseCase를 순차 호출. 셋 다 성공 시 `Result.success(DownloadAllResult(...))`, 하나라도 실패 시 `Result.failure`.

### 3.6 Hilt: ReceiverModule

- **파일:** [ReceiverModule.kt](app/src/main/java/com/kuit/afternote/feature/receiver/data/di/ReceiverModule.kt)
- **변경:**  
`ReceivedMindRecordRepository` 바인딩 추가:  
`bindReceivedMindRecordRepository(impl: ReceivedMindRecordRepositoryImpl): ReceivedMindRecordRepository`

---

## Part 4: ViewModel · UI

### 4.1 ViewModel: ReceiverDownloadAllViewModel

- **파일 (신규):** `feature/receiver/presentation/viewmodel/ReceiverDownloadAllViewModel.kt`
- **의존성:** DownloadAllReceivedUseCase.
- **receiverId:** 생성자로 받지 않고, **메서드 인자로 받음.**  
`fun confirmDownloadAll(receiverId: Long)` → UseCase(receiverId) 호출. (Screen에서 `receiverId`를 인자로 넘기면 되므로 SavedStateHandle/Assisted 불필요.)
- **State:** `UiState(isLoading, errorMessage, downloadSuccess 등).`  
`confirmDownloadAll` 호출 시 isLoading = true → 성공 시 downloadSuccess 처리 후 다이얼로그 닫기, 실패 시 errorMessage 설정.

### 4.2 ReceiverAfternoteScreen 연동

- **ReceiverAfternoteScreen**에서 `ReceiverDownloadAllViewModel`을 `hiltViewModel()`으로 주입.
- `uiState`를 `collectAsStateWithLifecycle()`로 구독.
- 다이얼로그 "예" 버튼: `viewModel.confirmDownloadAll(receiverId.toLongOrNull() ?: return@Button)` 호출 후 `showDialog = false` (또는 성공 콜백에서 닫기).
- 로딩 중: 다이얼로그에 로딩 표시 또는 버튼 비활성화.
- `errorMessage`가 있으면 토스트/스낵바로 표시.

---

## 구현 순서 (체크리스트)


| #   | 작업                                                                                                 |     |
| --- | -------------------------------------------------------------------------------------------------- | --- |
| 1   | ContentSection에 `onButtonClick` 추가                                                                 |     |
| 2   | ReceiverAfternoteScreen에 탭 전환 콜백 3개 추가 및 ContentSection에 연결                                        |     |
| 3   | ReceiverMainRoute에서 HOME 호출 시 위 콜백 + receiverId 전달                                                 |     |
| 4   | ReceivedMindRecord 엔티티, ReceivedMindRecordRepository 인터페이스, ReceivedMapper.toReceivedMindRecord 추가 |     |
| 5   | ReceivedMindRecordRepositoryImpl 구현, ReceiverModule에 바인딩                                           |     |
| 6   | GetReceivedMindRecordsUseCase 신설                                                                   |     |
| 7   | DownloadAllReceivedUseCase 신설 (DownloadAllResult data class 포함)                                    |     |
| 8   | ReceiverDownloadAllViewModel 신설, UiState 정의                                                        |     |
| 9   | ReceiverAfternoteScreen에서 ViewModel 주입, 다이얼로그 "예"에서 confirmDownloadAll(receiverId) 호출, 로딩·에러 UI 처리 |     |


---

## 참고: 기존 구조

- 타임레터: [ReceivedTimeLetterRepository](app/src/main/java/com/kuit/afternote/feature/receiver/domain/repository/iface/ReceivedTimeLetterRepository.kt) → ReceivedTimeLetterRepositoryImpl → GetReceivedTimeLettersUseCase.
- 애프터노트: ReceivedAfternoteRepository → ReceivedAfternoteRepositoryImpl → GetReceivedAfterNotesUseCase.
- 마인드레코드: Data의 [ReceivedRepository.getReceivedMindRecords](app/src/main/java/com/kuit/afternote/feature/receiver/data/repository/iface/ReceivedRepository.kt)만 존재. Domain Repository + UseCase는 이번에 신설.

