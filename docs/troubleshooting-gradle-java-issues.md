# Gradle Java 문제 해결 기록

## 문제
Windows에서 pre-commit hook 실행 시 `java.lang.InternalError: Error loading java.security file` 에러 발생

## 시도한 해결책들

### 실패한 방법들
1. gradle.properties에 JBR 경로 설정
2. pre-commit hook에서 Windows 감지 및 gradlew.bat 사용
3. JAVA_HOME 환경 변수 설정
4. JAVA_TOOL_OPTIONS로 보안 정책 우회
5. org.gradle.daemon=false 설정
6. org.gradle.jvmargs에 보안 옵션 추가

### 성공한 해결책
**Eclipse Adoptium JDK 사용**
- 경로: `C:/Users/rlfjr/AppData/Local/Programs/Eclipse Adoptium/jdk-21.0.9.10-hotspot`
- gradle.properties: `org.gradle.java.home=C:/Users/rlfjr/AppData/Local/Programs/Eclipse Adoptium/jdk-21.0.9.10-hotspot`

## 원인
Android Studio의 JBR은 명령줄에서 Java 보안 파일 로드 문제가 있음. Eclipse Adoptium JDK 사용으로 해결.

