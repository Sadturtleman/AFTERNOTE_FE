import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val properties =
    Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$rootDir/config/detekt.yml"))
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        html.outputLocation.set(
            file("$rootDir/reports/detekt/${project.name}.html"),
        )

        sarif.required.set(true)
        sarif.outputLocation.set(
            file("$rootDir/reports/detekt/${project.name}.sarif"),
        )

        xml.required.set(false)
        txt.required.set(false)
    }
}

android {
    namespace = "com.kuit.afternote"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.kuit.afternote"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val useMockApi = project.findProperty("USE_MOCK_API") as? String ?: "false"
        buildConfigField("Boolean", "USE_MOCK_API", useMockApi)

        // Test credentials from local.properties (not committed to git)
        val testEmail = project.findProperty("TEST_EMAIL") as? String ?: ""
        val testPassword = project.findProperty("TEST_PASSWORD") as? String ?: ""
        buildConfigField("String", "TEST_EMAIL", "\"$testEmail\"")
        buildConfigField("String", "TEST_PASSWORD", "\"$testPassword\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.add("-Xannotation-default-target=param-property")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

// Java Toolchain 설정 제거: Foojay Resolver 플러그인과의 호환성 문제로 제거
// compileOptions에서 Java 11을 명시적으로 설정하여 동일한 효과 달성
// 필요시 각 개발자는 ~/.gradle/gradle.properties에 org.gradle.java.home 설정

dependencies {
    // ---------------------------------------------------------------
    // Core Library Desugaring
    // ---------------------------------------------------------------
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // ---------------------------------------------------------------
    // Implementation (Main)
    // ---------------------------------------------------------------
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.runtime)
    ksp(libs.hilt.android.compiler)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.lifecycle.viewmodel.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.compose.wheel.picker)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)

    // ---------------------------------------------------------------
    // Test Implementation (Unit Tests)
    // ---------------------------------------------------------------
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    // ---------------------------------------------------------------
    // Android Test Implementation (Instrumentation Tests)
    // ---------------------------------------------------------------
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // ---------------------------------------------------------------
    // Debug Implementation
    // ---------------------------------------------------------------
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
