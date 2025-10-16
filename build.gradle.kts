plugins {
    // подключаем плагины только в classpath (apply false),
    // чтобы они были видимы модулям
    id("com.android.application") version "8.13.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false

    // критично важно: объявляем Hilt и KSP здесь
    id("com.google.dagger.hilt.android") version "2.56" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.25" apply false
}