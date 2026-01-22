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
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
}

// Java Toolchain 설정 제거: Foojay Resolver 플러그인과의 호환성 문제로 제거
// compileOptions에서 Java 11을 명시적으로 설정하여 동일한 효과 달성
// 필요시 각 개발자는 ~/.gradle/gradle.properties에 org.gradle.java.home 설정

dependencies {
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.room.common.jvm)
    // Core Library Desugaring (Java 8+ API 지원)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
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
    implementation(libs.hilt.navigation.compose)
    // Wheel Picker
    implementation("com.github.zj565061763:compose-wheel-picker:1.0.0-beta05")
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("io.mockk:mockk:1.13.8")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.appcompat:appcompat:1.6.1")
}
