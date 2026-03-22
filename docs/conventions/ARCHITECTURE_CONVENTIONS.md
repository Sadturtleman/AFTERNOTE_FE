# 아키텍처 컨벤션

이 프로젝트의 레이어 구조, 네이밍, 의존 방향 규칙을 정리한다.
Android/Kotlin 공식 컨벤션을 최우선으로 하며, 그 안에서 클린 아키텍처를 적용한다.

## 레이어 구조

```
presentation → domain ← data
```

- **domain**은 독립적이다. data, presentation을 import하지 않는다.
- **data**는 domain을 알고, domain이 정한 인터페이스(Repository)를 구현한다.
- **presentation**은 domain을 알고, UseCase를 통해 비즈니스 로직을 호출한다.

## 패키지 구조

```
com.kuit.afternote.feature.{기능명}
├── data
│   ├── dto              # 서버 요청/응답 DTO (@Serializable)
│   ├── mapper            # DTO ↔ Domain 변환 (확장 함수)
│   ├── repositoryimpl    # Repository 구현체
│   └── service           # Retrofit API interface
├── domain
│   ├── model             # 순수 도메인 모델 (data class)
│   ├── repository        # Repository 인터페이스
│   └── usecase           # UseCase
└── presentation
    ├── uimodel           # UI 전용 모델
    └── viewmodel         # ViewModel
```

## UseCase

- **모든 API에 UseCase를 둔다.** 단순 CRUD라도 예외 없이 UseCase를 거친다.
- 네이밍: `동사(현재형) + 명사 + UseCase`
  - 예: `CreateSocialAfternoteUseCase`, `GetAfternoteDetailUseCase`
- `operator fun invoke`로 구현하여 함수처럼 호출한다.
- **무상태(Stateless)**: 내부에 가변 변수를 두지 않는다.
- ViewModel은 Repository를 직접 주입받지 않고, UseCase만 주입받는다.

```kotlin
class GetUserUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(userId: String): Result<User> =
        repository.getUser(userId)
}
```

## 도메인 모델 (Domain Model)

- 순수 Kotlin `data class`로 작성한다.
- **금지 사항:**
  - `@Serializable`, `@SerialName` 등 직렬화 어노테이션
  - `@Entity` 등 DB 어노테이션
  - `Parcelable` 구현
  - `android.*`, `retrofit2.*` 등 프레임워크 import
- DTO가 변해도 도메인 모델은 안정적으로 유지되어야 한다.

```kotlin
// ✅ domain/model
data class AfternoteDetail(
    val id: Long,
    val title: String,
    val timestamps: AfternoteDetailTimestamps,
)

// ❌ domain/model에 직렬화 어노테이션 사용
data class AfternoteDetail(
    @SerialName("id") val id: Long,
)
```

## 매퍼 (Mapper)

- **위치**: data 레이어(`data/mapper/`) 또는 presentation 레이어에 둔다. domain에는 두지 않는다.
- **방식**: 확장 함수를 사용한다.
- **네이밍**:
  - domain → DTO: `toDto()`
  - DTO → domain: `toDomain()`

```kotlin
// data/mapper/AfternoteInputMapper.kt
fun AfternotePlaylistInput.toDto(): AfternotePlaylist =
    AfternotePlaylist(
        profilePhoto = profilePhoto,
        atmosphere = atmosphere,
    )
```

## DTO (Data Transfer Object)

- data 레이어에만 위치한다(`data/dto/`).
- `@Serializable`, `@SerialName`은 DTO에서만 사용한다.
- 생활체조 원칙 중 3번(원시값 포장), 7번(인스턴스 변수 제한), 8번(일급 컬렉션), 9번(getter/setter 금지)은 DTO에 한해 완화한다.

## Repository

- **인터페이스**: `domain/repository/`에 위치. data 레이어를 import하지 않는다.
- **구현체**: `data/repositoryimpl/`에 위치. DTO → domain 변환을 여기서 한다.
- 파라미터가 3개 이상이면 Input 객체(`domain/model/`)로 묶는다.

```kotlin
// ✅ Input 객체 사용
suspend fun createSocial(input: CreateSocialInput): Result<Long>

// ❌ 파라미터 나열
suspend fun createSocial(
    title: String,
    processMethod: String,
    actions: List<String>,
    leaveMessage: String?,
    credentialsId: String?,
    credentialsPassword: String?,
    receiverIds: List<Long>,
): Result<Long>
```
