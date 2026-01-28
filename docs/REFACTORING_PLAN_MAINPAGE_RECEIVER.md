# Refactoring Plan: Main Page & Receiver Components

## Overview
This document outlines a step-by-step plan to extract common patterns from the main page and receiver components into reusable components.

## Common Patterns Identified

### 1. **Scaffold Structure Pattern**
Both screens share:
- `Scaffold` with `topBar` (TopBar) and `bottomBar` (BottomNavigationBar)
- Similar padding handling with `paddingValues`
- Content wrapped in `Box` or `Column` with consistent spacing

**Current Implementation:**
```kotlin
// Main Page
Scaffold(
    topBar = { TopBar(title = "애프터노트") },
    bottomBar = { BottomNavigationBar(...) }
) { paddingValues ->
    Box(modifier = Modifier.padding(...)) {
        // Content
    }
}

// Receiver
Scaffold(
    topBar = { TopBar(...) },
    bottomBar = { BottomNavigationBar(...) }
) { innerPadding ->
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        // Content
    }
}
```

### 2. **Content Layout Pattern**
- Horizontal padding: `20.dp` (consistent across both)
- Vertical spacing: `Spacing.m` or `16.dp` for top spacing
- Bottom padding: `paddingValues.calculateBottomPadding() + 16.dp` (main page)
- Top padding: `paddingValues.calculateTopPadding()` (main page)

### 3. **List Content Pattern**
- Both use `LazyColumn` with `items()` for lists
- Similar spacing: `Arrangement.spacedBy(8.dp)` or `12.dp`
- Similar content padding patterns

### 4. **State Management Pattern**
- Both manage `selectedBottomNavItem` state
- Similar event handling for navigation

## Step-by-Step Refactoring Plan

### Phase 1: Create Base Scaffold Component (Low Risk)
**Goal:** Extract the common Scaffold structure

**New Component:** `BaseScreenScaffold` in `core/ui/component/`

**Features:**
- Accepts `topBar`, `bottomBar`, and `content` as slots
- Handles consistent padding patterns
- Provides flexible content area

**Benefits:**
- Reduces duplication
- Ensures consistent padding across screens
- Easy to test and maintain

**Files to Create:**
- `app/src/main/java/com/kuit/afternote/core/ui/component/BaseScreenScaffold.kt`

**Files to Refactor:**
- `AfternoteMainScreen.kt` (mainpage)
- `ReceiverAfterNoteMainScreen.kt` (receiver)
- `AfterNoteListScreen.kt` (receiver)
- `AfterNoteScreen.kt` (receiver)

---

### Phase 2: Create Content Container Component (Medium Risk)
**Goal:** Extract common content layout patterns

**New Component:** `ScreenContentContainer` in `core/ui/component/`

**Features:**
- Handles horizontal padding (20.dp)
- Handles top/bottom padding from Scaffold
- Supports both `Column` and `LazyColumn` content
- Optional FAB button support

**Benefits:**
- Consistent spacing across screens
- Reduces layout code duplication

**Files to Create:**
- `app/src/main/java/com/kuit/afternote/core/ui/component/ScreenContentContainer.kt`

**Files to Refactor:**
- Same as Phase 1

---

### Phase 3: Create List Screen Template (Medium Risk)
**Goal:** Extract common list screen patterns

**New Component:** `ListScreenTemplate` in `core/ui/component/`

**Features:**
- Tab/Filter row at top (optional)
- LazyColumn with consistent spacing
- Empty state support (optional)
- FAB button support (optional)

**Benefits:**
- Standardizes list screen structure
- Reduces boilerplate

**Files to Create:**
- `app/src/main/java/com/kuit/afternote/core/ui/component/ListScreenTemplate.kt`

**Files to Refactor:**
- `AfternoteMainScreen.kt` (mainpage - uses tabs)
- `AfterNoteListScreen.kt` (receiver - uses filter chips)

---

### Phase 4: Consolidate State Management (Low Risk)
**Goal:** Extract common bottom navigation state handling

**New Component:** `BottomNavStateHolder` (if needed)

**Note:** This might not be necessary if state is already in ViewModel. Review current implementation first.

---

## Detailed Implementation Plan

### Step 1: Create `BaseScreenScaffold` Component

**Location:** `app/src/main/java/com/kuit/afternote/core/ui/component/BaseScreenScaffold.kt`

**API Design:**
```kotlin
@Composable
fun BaseScreenScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit? = null,
    content: @Composable (PaddingValues) -> Unit
)
```

**Implementation Strategy:**
1. Create the component with slot API pattern
2. Test with one screen (start with receiver - simpler)
3. Migrate remaining screens one by one
4. Remove old code after all migrations complete

---

### Step 2: Create `ScreenContentContainer` Component

**Location:** `app/src/main/java/com/kuit/afternote/core/ui/component/ScreenContentContainer.kt`

**API Design:**
```kotlin
@Composable
fun ScreenContentContainer(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    horizontalPadding: Dp = 20.dp,
    topPadding: Dp? = null, // null = use paddingValues.calculateTopPadding()
    bottomPadding: Dp? = null, // null = use paddingValues.calculateBottomPadding() + 16.dp
    fab: @Composable () -> Unit? = { null },
    content: @Composable () -> Unit
)
```

**Implementation Strategy:**
1. Create component with flexible padding options
2. Support both Column and LazyColumn content
3. Migrate screens incrementally

---

### Step 3: Create `ListScreenTemplate` Component (Optional)

**Location:** `app/src/main/java/com/kuit/afternote/core/ui/component/ListScreenTemplate.kt`

**API Design:**
```kotlin
@Composable
fun <T> ListScreenTemplate(
    modifier: Modifier = Modifier,
    items: List<T>,
    topContent: @Composable () -> Unit? = { null }, // Tabs, filters, etc.
    itemContent: @Composable (T) -> Unit,
    emptyContent: @Composable () -> Unit? = { null },
    spacing: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp)
)
```

**Note:** This might be too specific. Consider if the abstraction is worth it.

---

## Migration Strategy

### Approach: Incremental Migration (Low Risk)

1. **Create new components** in `core` module first
2. **Test with one screen** (start with simplest - `AfterNoteScreen.kt`)
3. **Verify behavior** matches original
4. **Migrate remaining screens** one by one
5. **Remove old code** after all migrations complete

### Testing Strategy

For each migration:
1. Build and run the app
2. Navigate to the migrated screen
3. Verify:
   - Padding is correct
   - Navigation works
   - Content displays correctly
   - Bottom nav works
   - Top bar works

---

## Risk Assessment

| Phase | Risk Level | Reason |
|-------|------------|--------|
| Phase 1: BaseScreenScaffold | Low | Simple wrapper, easy to test |
| Phase 2: ScreenContentContainer | Medium | Padding logic needs careful handling |
| Phase 3: ListScreenTemplate | Medium-High | Might be over-abstracting |

## Recommendation

**Start with Phase 1 only.** This gives immediate benefits with minimal risk:
- Reduces duplication
- Ensures consistency
- Easy to rollback if issues arise

After Phase 1 is stable, evaluate if Phase 2 is needed. Phase 3 might be unnecessary if the abstraction doesn't provide clear value.

---

## Example: After Phase 1 Migration

### Before:
```kotlin
@Composable
fun AfternoteMainScreen(...) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar(title = "애프터노트") },
        bottomBar = { BottomNavigationBar(...) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(...)) {
            // Content
        }
    }
}
```

### After:
```kotlin
@Composable
fun AfternoteMainScreen(...) {
    BaseScreenScaffold(
        modifier = modifier,
        topBar = { TopBar(title = "애프터노트") },
        bottomBar = { BottomNavigationBar(...) }
    ) { paddingValues ->
        // Content (padding handled by BaseScreenScaffold)
    }
}
```

---

## Next Steps

1. **Review this plan** and confirm approach
2. **Start with Phase 1** - Create `BaseScreenScaffold`
3. **Test with one screen** - Migrate `AfterNoteScreen.kt` first
4. **Iterate** - Migrate remaining screens one by one
5. **Evaluate** - Decide if Phase 2/3 are needed

---

## Questions to Consider

1. Are there other screens that could benefit from these components?
2. Should we handle FAB buttons in the base component or keep them separate?
3. Do we need different padding patterns for different screen types?
4. Should empty states be part of the base component or handled separately?
