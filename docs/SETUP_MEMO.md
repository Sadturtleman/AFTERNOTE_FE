### 1. 프로젝트 기초 세팅 패키징

![image.png](attachment:0c4d5988-9653-4964-a8ee-7354ada25a76:image.png)

```kotlin
┣ 📂 core
┃ ┣ 📂 component
┃ ┣ 📂 util
┣ 📂 data
┃ ┣ 📂 dto
┃ ┃ ┣ 📂 request
┃ ┃ ┣ 📂 response
┃ ┣ 📂 mapper
┃ ┣ 📂 repositoryimpl
┃ ┣ 📂 service
┣ 📂 di
┣ 📂 domain
┃ ┣ 📂 entity
┃ ┣ 📂 repository
┣ 📂 presentation
┃ ┣ 📂 diary
┃ ┣ 📂 home
┃ ┣ 📂 my
┃ ┣ 📂 navigation
```

### 2. 의존성 추가 (build.gradle, libs.versions.toml)

(Hilt 기초 세팅 관련도 함께 있음)

**project단 build.gradle**

- project단 build.gradle 파일
    
    ```kotlin
    // Top-level build file where you can add configuration options common to all sub-projects/modules.
    plugins {
        alias(libs.plugins.android.application) apply false
        alias(libs.plugins.kotlin.android) apply false
        alias(libs.plugins.kotlin.compose) apply false
        **alias(libs.plugins.ksp) apply false
        alias(libs.plugins.hilt) apply false**
    }
    ```
    

**app단 build.gradle**

- app단 build.gradle 파일
    
    ```kotlin
    import java.util.Properties
    
    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        alias(libs.plugins.kotlin.compose)
        **alias(libs.plugins.kotlin.serialization)**
        **alias(libs.plugins.hilt)
        alias(libs.plugins.ksp)**
    }
    
    **val properties = Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
    }**
    
    android {
        namespace = "com.konkuk.kuit6_project_setting_solution"
        compileSdk = 35
    
        defaultConfig {
            applicationId = "com.example.kuit6_project_setting_solution"
            minSdk = 24
            targetSdk = 36
            versionCode = 1
            versionName = "1.0"
    
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            **buildConfigField("String", "BASE_URL", properties["base.url"].toString())**
        }
    
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
        buildFeatures {
            compose = true
            **buildConfig = true**
        }
    }
    
    dependencies {
    
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
    
        **// navigation
        implementation(libs.androidx.navigation.compose)
    
        // serialization
        implementation(libs.kotlinx.serialization.json)
    
        // coil
        implementation(libs.coil.compose)
        implementation(libs.coil.network.okhttp)
    
        // Network
        implementation(platform(libs.okhttp.bom))
        implementation(libs.okhttp)
        implementation(libs.okhttp.logging.interceptor)
        implementation(libs.retrofit)
        implementation(libs.retrofit.kotlin.serialization.converter)
        implementation(libs.kotlinx.serialization.json)
    
        // Hilt
        implementation(libs.hilt.android)
        implementation(libs.hilt.core)
        implementation(libs.hilt.navigation.compose)
        ksp(libs.hilt.android.compiler)
        ksp(libs.hilt.compiler)
        ksp(libs.hilt.manager)**
    }
    ```
    

**libs.versions.toml**

- toml 파일
    
    ```kotlin
    [versions]
    navigationCompose = "2.8.9"
    okhttp = "4.11.0"
    retrofit = "2.9.0"
    retrofitKotlinSerializationConverter = "1.0.0"
    kotlinxSerializationJson = "1.8.0"
    coilCompose = "3.2.0"
    coilNetworkOkhttp = "3.2.0"
    hilt = "2.51"
    hiltNavigationCompose = "1.2.0"
    hiltManager = "1.0.0"
    ksp = "2.0.21-1.0.28"
    ****
    [libraries]
    androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
    okhttp-bom = { group = "com.squareup.okhttp3", name = "okhttp-bom", version.ref = "okhttp" }
    okhttp = { group = "com.squareup.okhttp3", name = "okhttp" }
    okhttp-logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor" }
    retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
    retrofit-kotlin-serialization-converter = { group = "com.jakewharton.retrofit", name = "retrofit2-kotlinx-serialization-converter", version.ref = "retrofitKotlinSerializationConverter" }
    kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerializationJson" }
    coil-compose = { group = "io.coil-kt.coil3", name = "coil-compose", version.ref = "coilCompose" }
    coil-network-okhttp = { group = "io.coil-kt.coil3", name = "coil-network-okhttp", version.ref = "coilNetworkOkhttp" }
    hilt-core = { group = "com.google.dagger", name = "hilt-core", version.ref = "hilt" }
    hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
    hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
    hilt-android-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
    hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version.ref = "hiltNavigationCompose" }
    hilt-manager = {group = "androidx.hilt" , name = "hilt-compiler", version.ref = "hiltManager"}
    
    [plugins]
    kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
    hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
    ksp = {id = "com.google.devtools.ksp", version.ref = "ksp"}
    ```
    

### 3. AndroidManifest.xml 구현

- 인터넷 권한 추가
    
    ```xml
    <uses-permission android:name="android.permission.INTERNET" />
    ```
    
- Cleartext HTTP 활성화를 통해 Http 접근 허용
    
    ```xml
    android:usesCleartextTraffic="true"
    ```
    

### 4. core 패키지 구현

- component : 공통 컴포넌트 구현
- util : 확장함수 구현
    - **ContextExt.kt**
        
        ```kotlin
        fun Context.**toast**(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        ```
        
    - **ModifierExt.kt**
        
        ```kotlin
        inline fun Modifier.**noRippleClickable**(
            crossinline onClick: () -> Unit
        ): Modifier = composed {
            clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
        }
        
        fun Modifier.**addFocusCleaner**(
            focusManager: FocusManager,
            doOnClear: () -> Unit = {}): Modifier {
            return this.pointerInput(Unit) {
                detectTapGestures(onTap = {
                    doOnClear()
                    focusManager.clearFocus()
                })
            }
        }
        ```
        

### 5. presentation 패키지 구현

- **Bottom Navigation** 구현
    - sealed class로 **Route** 구현
        
        ```kotlin
        sealed class Route(
            val route: String
        ) {
            data object Home: Route(route = "home")
        
            data object Diary: Route(route = "diary")
        
            data object My: Route(route = "my")
        }
        ```
        
    - **NavGraph** 구현 (HomeScreen, DiaryScreen, MyScreen까지 생성)
        
        ```kotlin
        @Composable
        fun KuitNavGraph(
            navController: NavHostController,
            modifier: Modifier = Modifier
        ) {
            NavHost(
                navController = navController,
                startDestination = Route.Home.route,
            ){
                composable(route = Route.Home.route) {
                    HomeScreen(modifier = modifier)
                }
                composable(route = Route.Diary.route) {
                    DiaryScreen(modifier = modifier)
                }
                composable(route = Route.My.route) {
                    MyScreen(modifier = modifier)
                }
            }
        }
        ```
        
    - **BottomNavItem data class** 구현
        
        ```kotlin
        data class BottomNavItem(
            val label: String,
            val route: String,
            val icon: Int
        )
        ```
        
    - **Scaffold**를 활용하여 **MainActivity**에서 **Bottom Navigation** 구조 구현
        
        ```kotlin
        val navController = rememberNavController()
        var selectedRoute by remember { mutableStateOf(Route.Home.route) }
        
        val bottomNavItems = listOf(
            BottomNavItem("홈", Route.Home.route, R.drawable.ic_home),
            BottomNavItem("일기", Route.Diary.route, R.drawable.ic_diary),
            BottomNavItem("마이페이지", Route.My.route, R.drawable.ic_my)
        )
        
        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = selectedRoute == item.route,
                            onClick = {
                                selectedRoute = item.route
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label
                                )
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            KuitNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
        ```
        

### 6. data 패키지 구현

- dto
    - request
    - response
        - cf) BaseResponse
- service
- mapper
- repositoryimpl

### 7. domain 패키지 구현

- entity
- repository

### 8. DI 적용 (Hilt)

- ApiModule, RepositoryModule, NetworkModule 구현
    
    ```kotlin
    @Module
    @InstallIn(SingletonComponent::class)
    object ApiModule {
        @Provides
        @Singleton
        fun providesHomeService(retrofit: Retrofit): HomeService =
            retrofit.create(HomeService::class.java)
    }
    ```
    
    ```kotlin
    @Module
    @InstallIn(SingletonComponent::class)
    **abstract class** RepositoryModule {
        **@Binds**
        @Singleton
        **abstract fun** bindsHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository
    }
    ```
    
    ```kotlin
    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {
        @Provides
        @Singleton
        fun providesOkHttpClient(): OkHttpClient {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
    
            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }
    
        @Provides
        @Singleton
        fun providesConverterFactory(): Converter.Factory =
            Json.asConverterFactory("application/json".toMediaType())
    
        @Provides
        @Singleton
        fun providesRetrofit(
            client: OkHttpClient,
            converterFactory: Converter.Factory
        ): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
    }
    ```
    
- KuitApplication 생성, MainActivity에 @AndroidEntryPoint 어노테이션 추가
- AndroidManifest.xml에서 KuitApplication 적용