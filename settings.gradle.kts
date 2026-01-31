pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // Convention 플러그인 대신 기본 Resolver 플러그인 사용
    // 명시적 toolchainManagement 설정과 함께 사용
    id("org.gradle.toolchains.foojay-resolver") version "1.0.0"
}

// 명시적으로 Toolchain Repository 설정 추가
// Convention 플러그인이 자동 설정을 주입하지 못하는 경우를 대비
toolchainManagement {
    jvm {
        javaRepositories {
            repository("foojay") {
                resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "afternote"
include(":app")
 