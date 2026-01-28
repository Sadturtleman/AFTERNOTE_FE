# API Delay Issues and Optimistic Updates - Cursor Rules Update Prompt

## Context

When implementing API calls in ViewModels, there's often a noticeable delay between user action and UI feedback because the code waits for the API response before updating the UI state. This creates a poor user experience, especially for operations that should feel instant.

## Problem

Current pattern in the codebase:
```kotlin
fun updateSomething() {
    viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        
        useCase.update()
            .onSuccess {
                _uiState.update { 
                    it.copy(isLoading = false, success = true) 
                }
            }
            .onFailure { e ->
                _uiState.update { 
                    it.copy(isLoading = false, errorMessage = e.message) 
                }
            }
    }
}
```

**Issue**: User must wait for the entire API round-trip (network latency + server processing) before seeing any feedback, even for operations that are likely to succeed.

## Solution: Optimistic Updates

Optimistic updates immediately show success in the UI, then make the API call in the background. If the API fails, rollback the optimistic state and show an error.

### Pattern

```kotlin
fun updateSomething() {
    viewModelScope.launch {
        // Optimistic update: 즉시 성공 상태로 설정
        _uiState.update { 
            it.copy(
                isLoading = false,
                success = true,
                needsRollback = false
            ) 
        }
        
        // 백그라운드에서 실제 API 호출
        useCase.update()
            .onSuccess {
                // 이미 성공 상태이므로 추가 업데이트 불필요
            }
            .onFailure { e ->
                // Rollback: 성공 상태를 취소하고 에러 표시
                _uiState.update {
                    it.copy(
                        success = false,
                        needsRollback = true,
                        errorMessage = mapErrorToUserMessage(e)
                    )
                }
            }
    }
}
```

### UiState Requirements

Add a `needsRollback: Boolean` flag to track when optimistic updates need to be rolled back:

```kotlin
data class MyUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
    val needsRollback: Boolean = false // Optimistic update 실패 시 rollback 필요
)
```

### Screen Handling

In the Screen, handle rollback before navigation:

```kotlin
LaunchedEffect(uiState.success, uiState.needsRollback) {
    // Rollback이 필요하면 navigation 취소
    if (uiState.needsRollback) {
        viewModel.clearSuccess()
        viewModel.clearRollback()
        return@LaunchedEffect
    }
    
    // 성공 시에만 navigation
    if (uiState.success && !uiState.needsRollback) {
        snackbarHostState.showSnackbar("작업이 완료되었습니다.")
        viewModel.clearSuccess()
        onNavigateAway()
    }
}
```

## When to Use Optimistic Updates

### ✅ Good Candidates
- **Non-critical updates**: Profile updates, settings changes, preferences
- **High success rate operations**: Operations that rarely fail (e.g., toggle switches)
- **User-initiated actions**: Actions where the user expects immediate feedback
- **Operations with rollback capability**: Can easily revert if API fails

### ❌ Avoid For
- **Security-critical operations**: Password changes, authentication (though can be used with careful rollback)
- **Financial transactions**: Payments, transfers
- **Irreversible operations**: Delete operations (unless with undo)
- **Operations requiring server validation**: Where client-side validation isn't sufficient

## Cursor Rules Update

Add the following section to `.cursor/rules/tech-stack/compose.mdc` or create a new file `.cursor/rules/tech-stack/optimistic-updates.mdc`:

```markdown
## Optimistic Updates Pattern (낙관적 업데이트 패턴) - Critical

### When to Use
- **Non-critical updates**: Profile, settings, preferences
- **High success rate operations**: Toggle switches, simple updates
- **User-initiated actions**: Where immediate feedback is expected

### Implementation Pattern

1. **UiState에 needsRollback 플래그 추가**:
```kotlin
data class MyUiState(
    val success: Boolean = false,
    val needsRollback: Boolean = false // Optimistic update 실패 시 rollback 필요
)
```

2. **ViewModel에서 Optimistic Update 구현**:
```kotlin
fun updateSomething() {
    viewModelScope.launch {
        // Optimistic: 즉시 성공 상태로 설정
        _uiState.update { 
            it.copy(success = true, needsRollback = false) 
        }
        
        // 백그라운드에서 실제 API 호출
        useCase.update()
            .onFailure { e ->
                // Rollback: 성공 상태 취소 및 에러 표시
                _uiState.update {
                    it.copy(
                        success = false,
                        needsRollback = true,
                        errorMessage = mapErrorToUserMessage(e)
                    )
                }
            }
    }
}
```

3. **Screen에서 Rollback 처리**:
```kotlin
LaunchedEffect(uiState.success, uiState.needsRollback) {
    if (uiState.needsRollback) {
        viewModel.clearSuccess()
        viewModel.clearRollback()
        return@LaunchedEffect
    }
    
    if (uiState.success && !uiState.needsRollback) {
        // Navigation or success feedback
        navController.navigate("success")
    }
}
```

### Rollback Handling
- **Navigation 취소**: `needsRollback`이 true면 navigation을 취소
- **에러 표시**: Rollback 시 에러 메시지를 사용자에게 표시
- **상태 초기화**: Rollback 후 `clearRollback()` 호출하여 상태 초기화

### Avoid For
- Security-critical operations (password changes - use with caution)
- Financial transactions
- Irreversible operations
- Operations requiring server-side validation
```

## Examples in Codebase

Current implementations that could benefit from optimistic updates:
- `ProfileViewModel.updateProfile()` - Profile updates
- `PushSettingsViewModel.updatePushSettings()` - Settings toggles
- `PasswordChangeViewModel.changePassword()` - Already implemented with optimistic updates

## Testing Considerations

When testing optimistic updates:
1. **Success path**: Verify immediate UI update, then API success
2. **Failure path**: Verify rollback occurs, error is shown, navigation is cancelled
3. **Race conditions**: Test rapid successive calls
4. **Network delays**: Test with slow network to ensure rollback works

## Migration Strategy

1. Identify ViewModels with API delays that affect UX
2. Add `needsRollback` flag to UiState
3. Implement optimistic update pattern
4. Update Screen to handle rollback
5. Test thoroughly, especially failure scenarios
6. Update tests to cover optimistic update paths
