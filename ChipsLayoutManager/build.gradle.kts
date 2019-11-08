import Deps.Coroutine.android
plugins {
    id("com.android.library")
}


android {
    compileSdkVersion(COMPILE_VERSION)

    defaultConfig {
        minSdkVersion(MIN_VERSION)
        targetSdkVersion(TARGET_VERSION)
        versionCode = VERSION_CODE
        versionName = VERSION_NAME
        //buildConfigField("String", "TINKER_ID", VERSION_NAME)
    }

    buildTypes {

        all {
            buildConfigField("boolean", "isLogEnabled", "false")
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

    }


}



dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.recyclerView)
}