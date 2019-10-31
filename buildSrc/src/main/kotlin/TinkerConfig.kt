import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.internal.impldep.bsh.commands.dir
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

/**
 * @author green sun
 *
 * @date 2019/10/29
 *
 * @desc
 */

const val TINKER_ENABLE = true


fun applyTinker(project: Project) {
//    if (TINKER_ENABLE) {
//        project.apply<TinkerPatchPlugin>()
//        configure<TinkerPatchExtension> {
//            // 基准包路径
//            oldApk = "${rootDir.absolutePath}/apk/release/base.apk"
//            // 新包路径
//            newApk = "${rootDir.absolutePath}/apk/release/new.apk"
//
//            ignoreWarning = false
//
//            useSign = true
//
//            tinkerEnable = TINKER_ENABLE
//
//            (this as ExtensionAware).configure<TinkerBuildConfigExtension> {
//                // 在编译新的apk时候，我们希望通过保持旧apk的proguard混淆方式，从而减少补丁包的大小。
//                // applyMapping = ""
//
//                // 可选参数；在编译新的apk时候，我们希望通过旧apk的R.txt文件保持ResId的分配，这样不仅可以减少补丁包的大小，同时也避免由于ResId改变导致remote view异常。
//                // applyResourceMapping = ""
//
//                tinkerId = VERSION_NAME
//
//                // 如果我们有多个dex,编译补丁时可能会由于类的移动导致变更增多。若打开keepDexApply模式，补丁包将根据基准包的类分布来编译。
//                keepDexApply = true
//
//                // 是否使用加固模式，仅仅将变更的类合成补丁。注意，这种模式仅仅可以用于加固应用中
//                isProtectedApp = false
//
//                // 是否支持新增非export的Activity
//                isSupportHotplugComponent = false
//            }
//
//
//            (this as ExtensionAware).configure<TinkerDexExtension> {
//                // 只能是'raw'或者'jar'。
//                //对于'raw'模式，我们将会保持输入dex的格式。
//                //对于'jar'模式，我们将会把输入dex重新压缩封装到jar。
//                // 如果你的minSdkVersion小于14，你必须选择‘jar’模式，而且它更省存储空间，但是验证md5时比'raw'模式耗时。
//                // 默认我们并不会去校验md5,一般情况下选择jar模式即可。
//                dexMode = "jar"
//
//                pattern = listOf("classes*.dex", "assets/secondary-dex-?.jar")
//
//                ReflectiveLintRunner.loader = DelegatingClassLoader(
//                        //use sample, let BaseBuildInfo unchangeable with tinker
//                        arrayOf(URL("tinker.sample.android.app.BaseBuildInfo"))
//                )
//            }
//
//            (this as ExtensionAware).configure<TinkerLibExtension> {
//                pattern = listOf("lib/*/*.so")
//            }
//
//            (this as ExtensionAware).configure<TinkerResourceExtension> {
//                pattern = listOf("res/*", "assets/*", "resources.arsc", "AndroidManifest.xml")
//
//                ignoreChange = listOf("assets/sample_meta.txt")
//
//                largeModSize = 100
//            }
//
//
//            (this as ExtensionAware).configure<TinkerPackageConfigExtension> {
//
//                configField("patchMessage", "tinker is sample to use")
//
//                configField("platform", "all")
//
//                configField("patchVersion", "1.0")
//            }
//
//
//
//            (this as ExtensionAware).configure<TinkerSevenZipExtension> {
//
//                zipArtifact = "com.tencent.mm:SevenZip:1.1.10"
//
//            }
//        }
//    }
}