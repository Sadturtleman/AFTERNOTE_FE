# Visual Testing Guide (시각적 테스트 가이드)

이 문서는 User API를 앱에서 시각적으로 테스트하는 방법을 설명합니다.

---

## ⚠️ 현재 상태

**문제**: `ProfileEditScreen`과 `PushToastSettingScreen`이 아직 ViewModel과 연결되지 않았습니다.

**해결 방법**: 아래 가이드를 따라 화면을 ViewModel에 연결하세요.

---

## 1. MockApiInterceptor 필요 여부

### MockApiInterceptor가 필요한 경우
- ✅ 백엔드 서버가 준비되지 않았을 때
- ✅ 네트워크 없이 빠르게 테스트하고 싶을 때
- ✅ 에러 시나리오 테스트 (401, 404 등)

### MockApiInterceptor가 필요 없는 경우
- ✅ 백엔드 서버가 준비되어 있고 실제 API로 테스트하고 싶을 때
- ✅ **권장**: 실제 서버로 테스트하려면 `gradle.properties`에서 `USE_MOCK_API=false`로 설정

---

## 2. ProfileEditScreen 연결하기

### 현재 문제
- `ProfileEditScreen`이 `ProfileViewModel`을 사용하지 않음
- 프로필 데이터를 로드하지 않음
- 프로필 수정 기능이 동작하지 않음

### 해결 방법

**Step 1: ProfileEditScreen 수정**

`ProfileEditScreen.kt`를 다음과 같이 수정:

```kotlin
@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    callbacks: ProfileEditCallbacks = ProfileEditCallbacks()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    
    // 화면 진입 시 프로필 로드 (userId는 JWT 토큰에서 자동 추출)
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    
    // TextFieldState를 uiState와 동기화
    val nameState = rememberTextFieldState(initialText = uiState.name)
    val contactState = rememberTextFieldState(initialText = uiState.phone ?: "")
    val emailState = rememberTextFieldState(initialText = uiState.email)
    
    // ... 기존 Scaffold 코드 ...
    
    // 등록 버튼 클릭 시 프로필 업데이트 (userId는 JWT 토큰에서 자동 추출)
    val onRegisterClick = {
        viewModel.updateProfile(
            name = nameState.text.takeIf { it.isNotBlank() },
            phone = contactState.text.takeIf { it.isNotBlank() },
            profileImageUrl = null // TODO: 이미지 업로드 구현 후
        )
    }
    
    Scaffold(
        topBar = {
            TopBar(
                title = "프로필 수정",
                onBackClick = callbacks.onBackClick,
                onActionClick = onRegisterClick, // 수정된 콜백 사용
                actionText = "등록"
            )
        }
    ) { paddingValues ->
        // ... 기존 UI 코드 ...
        
        // 에러 메시지 표시 (Snackbar 또는 Toast)
        uiState.errorMessage?.let { error ->
            // Snackbar 표시 로직
        }
        
        // 로딩 상태 표시
        if (uiState.isLoading) {
            // 로딩 인디케이터
        }
    }
}
```

**Step 2: 필요한 Import 추가**

```kotlin
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import com.kuit.afternote.feature.user.presentation.viewmodel.ProfileViewModel
```

---

## 3. PushToastSettingScreen 연결하기

### 현재 문제
- `PushToastSettingScreen`이 `PushSettingsViewModel`을 사용하지 않음
- 로컬 상태만 사용하여 실제 API와 동기화되지 않음

### 해결 방법

**Step 1: PushToastSettingScreen 수정**

`PushToastSettingScreen.kt`를 다음과 같이 수정:

```kotlin
@Composable
fun PushToastSettingScreen(
    viewModel: PushSettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 화면 진입 시 푸시 설정 로드 (userId는 JWT 토큰에서 자동 추출)
    LaunchedEffect(Unit) {
        viewModel.loadPushSettings()
    }
    
    Scaffold(
        topBar = {
            TopBar("푸시 알림 설정", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            // 타임레터
            PushSettingRow(
                name = "타임레터",
                isChecked = uiState.timeLetter,
                onCheckedChange = { newValue ->
                    viewModel.updatePushSettings(
                        timeLetter = newValue,
                        mindRecord = null,
                        afterNote = null
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // 마음의 기록
            PushSettingRow(
                name = "마음의 기록",
                isChecked = uiState.mindRecord,
                onCheckedChange = { newValue ->
                    viewModel.updatePushSettings(
                        timeLetter = null,
                        mindRecord = newValue,
                        afterNote = null
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // 애프터노트
            PushSettingRow(
                name = "애프터노트",
                isChecked = uiState.afterNote,
                onCheckedChange = { newValue ->
                    viewModel.updatePushSettings(
                        timeLetter = null,
                        mindRecord = null,
                        afterNote = newValue
                    )
                }
            )
            
            // 에러 메시지 표시
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
```

**Step 2: 필요한 Import 추가**

```kotlin
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import com.kuit.afternote.feature.user.presentation.viewmodel.PushSettingsViewModel
```

---

## 4. 테스트 방법

### Step 1: Mock API 모드로 테스트 (선택)

```bash
# gradle.properties
USE_MOCK_API=true
```

### Step 2: 실제 서버로 테스트 (권장)

```bash
# gradle.properties
USE_MOCK_API=false
```

### Step 3: 앱 실행 및 테스트

1. **앱 실행**
   ```bash
   ./gradlew :app:installDebug
   ```

2. **로그인**
   - DevMode 화면에서 빠른 로그인 사용
   - 또는 일반 로그인 화면에서 로그인

3. **프로필 수정 화면 테스트**
   - 설정 → 프로필 수정
   - 프로필 정보가 로드되는지 확인
   - 이름/연락처 수정 후 "등록" 버튼 클릭
   - 수정 성공 여부 확인

4. **푸시 알림 설정 화면 테스트**
   - 설정 → 푸시 알림 설정
   - 푸시 설정이 로드되는지 확인
   - 스위치를 토글하여 설정 변경
   - 변경 사항이 저장되는지 확인

---

## 5. 확인 사항

### ✅ 성공 시나리오
- [ ] 프로필 수정 화면에서 프로필 정보가 표시됨
- [ ] 프로필 수정 후 "등록" 버튼 클릭 시 성공 메시지 표시
- [ ] 푸시 알림 설정 화면에서 현재 설정이 표시됨
- [ ] 스위치 토글 시 설정이 즉시 저장됨

### ❌ 실패 시나리오 (에러 처리 확인)
- [ ] 네트워크 오류 시 에러 메시지 표시
- [ ] 401 Unauthorized 시 에러 메시지 표시
- [ ] 404 Not Found 시 에러 메시지 표시

---

## 6. ✅ 해결된 사항

### userId 자동 추출

**이제 `userId`는 JWT 토큰에서 자동으로 추출됩니다!**

- `JwtDecoder` 유틸리티가 JWT 토큰을 디코딩하여 `userId`를 추출합니다
- `TokenManager.getUserId()` 메서드를 통해 `userId`를 가져올 수 있습니다
- ViewModel은 자동으로 `TokenManager`에서 `userId`를 가져와 사용합니다
- 화면에서는 `userId`를 전달할 필요가 없습니다

**구현된 기능:**
- ✅ `JwtDecoder.getUserId(token)` - JWT 토큰에서 userId 추출
- ✅ `TokenManager.getUserId()` - 저장된 토큰에서 userId 가져오기
- ✅ `ProfileViewModel.loadProfile()` - userId 파라미터 불필요
- ✅ `PushSettingsViewModel.loadPushSettings()` - userId 파라미터 불필요

---

## 7. TimeLetter API 테스트

**참고**: TimeLetter API의 Domain/UI 레이어는 박경민님의 담당입니다. Data 레이어만 구현되어 있으므로, UI 테스트는 Domain/UI 레이어 구현 후 가능합니다.

---

## 요약

1. **MockApiInterceptor**: 백엔드가 준비되어 있으면 `USE_MOCK_API=false`로 설정하여 실제 서버로 테스트
2. **화면 연결**: `ProfileEditScreen`과 `PushToastSettingScreen`을 각각의 ViewModel에 연결
3. **userId 자동 추출**: ✅ JWT 토큰에서 자동으로 `userId`를 추출하므로 하드코딩 불필요
4. **시각적 확인**: 앱 실행 후 설정 화면에서 프로필 수정 및 푸시 알림 설정 기능 테스트
