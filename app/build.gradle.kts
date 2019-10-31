import Deps.Tinker.lib
import com.android.tools.build.bundletool.model.SigningConfiguration
import com.android.tools.lint.gradle.api.DelegatingClassLoader
import com.android.tools.lint.gradle.api.ReflectiveLintRunner
import com.tencent.tinker.build.gradle.TinkerPatchPlugin
import com.tencent.tinker.build.gradle.extension.*
import java.net.URL

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(COMPILE_VERSION)

    defaultConfig {
        applicationId = "com.lyj.fakepixiv"
        minSdkVersion(MIN_VERSION)
        targetSdkVersion(TARGET_VERSION)
        versionCode = VERSION_CODE
        versionName = VERSION_NAME
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        //buildConfigField("String", "TINKER_ID", VERSION_NAME)
    }

    signingConfigs {
        create("release") {
            storeFile = File("../greensun.jks")
            storePassword ="liaolove1314"
            keyAlias = "greensun"
            keyPassword = "liaolove1314"
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_7)
        setTargetCompatibility(JavaVersion.VERSION_1_7)
    }

    dexOptions {
        jumboMode = true
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/license.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/proguard/androidx-annotations.pro")
        exclude("META-INF/atomicfu.kotlin_module")
    }

    kapt {
        javacOptions {
            // Increase the max count of errors from annotation processors.
            // Default is 100.
            option("-Xmaxerrs", 500)
        }
    }

    dexOptions {
        javaMaxHeapSize = "4g"
    }
}


kapt {
    generateStubs = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Vers.kotlin_version}")
    addDeps(project, Deps)
    implementation(project(":ChipsLayoutManager"))
    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}



if (TinkerConfig.TINKER_ENABLE) {
    apply<TinkerPatchPlugin>()
    configure<TinkerPatchExtension> {
        // 基准包路径
        oldApk = "${rootDir.absolutePath}/apk/release/base.apk"
        // 新包路径
        newApk = "${rootDir.absolutePath}/apk/release/new.apk"

        ignoreWarning = false

        useSign = true

        tinkerEnable = TinkerConfig.TINKER_ENABLE

        allowLoaderInAnyDex = true

        removeLoaderForAllDex = true

        (this as ExtensionAware).configure<TinkerBuildConfigExtension> {
            // 在编译新的apk时候，我们希望通过保持旧apk的proguard混淆方式，从而减少补丁包的大小。
            applyMapping = "${rootDir.absolutePath}/mapping/app-release-mapping.txt"

            // 可选参数；在编译新的apk时候，我们希望通过旧apk的R.txt文件保持ResId的分配，这样不仅可以减少补丁包的大小，同时也避免由于ResId改变导致remote view异常。
            applyResourceMapping = "${rootDir.absolutePath}/mapping/app-release-R.txt"

            tinkerId = VERSION_NAME

            // 如果我们有多个dex,编译补丁时可能会由于类的移动导致变更增多。若打开keepDexApply模式，补丁包将根据基准包的类分布来编译。
            keepDexApply = true

            // 是否使用加固模式，仅仅将变更的类合成补丁。注意，这种模式仅仅可以用于加固应用中
            isProtectedApp = false

            // 是否支持新增非export的Activity
            isSupportHotplugComponent = false
        }


        (this as ExtensionAware).configure<TinkerDexExtension> {
            // 只能是'raw'或者'jar'。
            //对于'raw'模式，我们将会保持输入dex的格式。
            //对于'jar'模式，我们将会把输入dex重新压缩封装到jar。
            // 如果你的minSdkVersion小于14，你必须选择‘jar’模式，而且它更省存储空间，但是验证md5时比'raw'模式耗时。
            // 默认我们并不会去校验md5,一般情况下选择jar模式即可。
            dexMode = "jar"

            // 需要处理dex路径，支持*、?通配符，必须使用'/'分割。路径是相对安装包的，例如assets/...
            pattern = mutableListOf("classes*.dex", "assets/secondary-dex-?.jar")

            // 这一项非常重要，它定义了哪些类在加载补丁包的时候会用到。这些类是通过Tinker无法修改的类，也是一定要放在main dex的类。
            //这里需要定义的类有：
            //1. 你自己定义的Application类；
            //2. Tinker库中用于加载补丁包的部分类，即com.tencent.tinker.loader.*；
            //3. 如果你自定义了TinkerLoader，需要将它以及它引用的所有类也加入loader中；
            //4. 其他一些你不希望被更改的类，例如Sample中的BaseBuildInfo类。这里需要注意的是，这些类的直接引用类也需要加入到loader中。或者你需要将这个类变成非preverify。
            //5. 使用1.7.6版本之后的gradle版本，参数1、2会自动填写。若使用newApk或者命令行版本编译，1、2依然需要手动填写
            loader = mutableListOf("com.lyj.fakepixiv.app.App", "com.tencent.tinker.loader.*")
        }

        (this as ExtensionAware).configure<TinkerLibExtension> {
            pattern = mutableListOf("lib/*/*.so")
        }

        (this as ExtensionAware).configure<TinkerResourceExtension> {
            // 需要处理res路径，支持*、?通配符，必须使用'/'分割。与dex.pattern一致, 路径是相对安装包的，
            // 例如assets/...，务必注意的是，只有满足pattern的资源才会放到合成后的资源包。
            pattern = mutableListOf("res/*", "assets/*", "resources.arsc", "AndroidManifest.xml")

            //ignoreChange = listOf("assets/sample_meta.txt")

            // 对于修改的资源，如果大于largeModSize，我们将使用bsdiff算法。这可以降低补丁包的大小，但是会增加合成时的复杂度。默认大小为100kb
            largeModSize = 100
        }


        (this as ExtensionAware).configure<TinkerPackageConfigExtension> {

            configField("patchMessage", "tinker is sample to use")

            configField("platform", "all")

            configField("patchVersion", "1.0")
        }



        (this as ExtensionAware).configure<TinkerSevenZipExtension> {
            // 例如"com.tencent.mm:SevenZip:1.1.10"，将自动根据机器属性获得对应的7za运行文件，推荐使用。
            zipArtifact = "com.tencent.mm:SevenZip:1.1.10"

            // 系统中的7za路径，例如"/usr/local/bin/7za"。path设置会覆盖zipArtifact，若都不设置，将直接使用7za去尝试。
            // path = ""
        }
    }
}


