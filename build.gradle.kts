// Файл: build.gradle.kts (в корне проекта)
// ✅ ТОЛЬКО один вариант каждого плагина, без дубликатов.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
