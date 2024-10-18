pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://www.jitpack.io")
        maven(url = "https://maven.google.com")
        maven(url = "$rootDir/..")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://www.jitpack.io")
        maven(url = "https://maven.google.com")
        maven(url = "$rootDir/..")
    }
}

rootProject.name = "Khwarazmi App"
include(":app")
include(":emtehanak")
