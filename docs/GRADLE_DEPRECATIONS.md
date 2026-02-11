# Gradle 10 Deprecation Warnings

This project shows deprecation warnings when building with Gradle 9.1. These come from **plugins**, not from this repo’s build scripts. The build is successful; the messages are about future Gradle 10 compatibility.

## Warnings (from `--warning-mode all`)

| Warning | Source                        | Action |
|--------|-------------------------------|--------|
| Multi-string dependency notation for `com.android.tools.lint:lint-gradle:31.13.0` | Android Gradle Plugin (AGP)   | Upgrade AGP when a Gradle-10–compatible version is available |
| Multi-string dependency notation for `com.android.tools.build:aapt2:8.13.0-13719691:osx` | Android Gradle Plugin (AGP)   | Same as above |
| `ReportingExtension.file(String)` deprecated; use `getBaseDirectory().file(String)` | A plugin (e.g. detekt)        | Upgrade the plugin when it uses the new API |

## What we’re *not* doing

- We are **not** using multi-string dependency notation in our own `build.gradle.kts` or `libs.versions.toml`; those usages are inside AGP.
- We are **not** calling `reporting.file()` in our scripts; that usage is inside a third‑party plugin.

## What to do

1. **Short term**  
   Builds remain valid. You can ignore these warnings or run with `--warning-mode all` only when you need the full list.

2. **Before Gradle 10**  
   Upgrade when new versions are available:
   - **AGP** – to a version that uses single-string dependency notation for its internal dependencies.
   - **detekt** – to versions that use `reporting.baseDirectory.file(...)` (or equivalent) instead of `ReportingExtension.file(String)`.

## How to reproduce

```bash
./gradlew :app:assembleDebug --warning-mode all
```

Individual deprecation messages will appear in the output.
