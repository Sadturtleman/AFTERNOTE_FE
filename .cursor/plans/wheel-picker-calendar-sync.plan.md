# 휠 피커 선택 날짜와 캘린더 동기화 (수정)

## 추가 요구사항: 연도 2026년부터 표시

휠 데이트 피커의 **연도 범위**를 **2026년부터 그 이후**만 보이도록 변경한다.

### 변경 위치

**파일:** [ReceiverWheelDatePickerDialog.kt](app/src/main/java/com/kuit/afternote/feature/receiver/presentation/component/ReceiverWheelDatePickerDialog.kt)

- **현재:** `YEAR_RANGE_PAST = 2`, `YEAR_RANGE_FUTURE = 2` → `years = (currentYear - 2 .. currentYear + 2).toList()` (예: 2023~2027).
- **변경:** 연도 리스트를 **2026년부터** 시작하도록 수정.
  - 예: `val minYear = 2026`, `val years = remember { (minYear..minYear + 10).toList() }` 또는 `(2026..2036).toList()` 등으로 2026 이후만 노출.
  - `initialDate`가 2026 미만이면 인덱스는 0(2026)으로 보정해 두기.

### 구체적 수정

1. `YEAR_RANGE_PAST` / `YEAR_RANGE_FUTURE` 제거 또는 무시.
2. 상수 추가: `private const val WHEEL_PICKER_MIN_YEAR = 2026`, (선택) `private const val WHEEL_PICKER_YEAR_COUNT = 11`.
3. `years` 계산: `(WHEEL_PICKER_MIN_YEAR until WHEEL_PICKER_MIN_YEAR + WHEEL_PICKER_YEAR_COUNT).toList()` 또는 `(2026..2036).toList()`.
4. `yearIndex` 계산 시 `initialDate.year`가 2026 미만이면 `years.indexOf(2026).coerceIn(0, years.lastIndex)` 또는 0으로 설정.

---

## 기존 플랜 요약: 휠 ↔ 캘린더 동기화

1. **CalendarGrid**: `displayYearMonth: YearMonth` 추가, 해당 월 일수·첫날 오프셋으로 그리드 표시.
2. **ReceiverDailyRecordScreen**: `selectedDate`를 `LocalDate`로, 휠 `onConfirm`으로 갱신, 버튼/캘린더에 반영.
3. **strings.xml**: "yyyy년 M월" 포맷 리소스 추가.

위 연도 범위 변경은 **ReceiverWheelDatePickerDialog**만 수정하면 되며, 캘린더/화면 연동 플랜과 함께 적용하면 된다.
