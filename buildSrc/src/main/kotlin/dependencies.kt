import jdk.internal.dynalink.linker.LinkerServices
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import kotlin.reflect.full.declaredMemberProperties

/**
 * @author green sun
 *
 * @date 2019/10/24
 *
 * @desc
 */

object Vers {
    const val kotlin_version = "1.3.30"
    const val gradle_version = "3.2.1"
    const val support_version = "28.0.0"
    const val rxJava_version = "2.2.4"
    const val rxKt_version = "2.3.0"
    const val coroutine_version = "1.3.0-M2"
    const val autoDispose_version = "1.1.0"
    const val retrofit_version = "2.6.0"
    const val glide_version = "4.9.0"
    const val gson_version = "2.8.5"
    const val moshi_version = "1.8.0"
    const val objectbox_version = "2.2.0"
    const val butterKnife_version = "9.0.0-rc3"
    const val fragmentation_version = "1.3.6"
    const val tinker_version = "1.9.14.3"
}

//class Dep(val core: List<String> = listOf(), val compiler: List<String> = listOf())

open class DepGroup
open class Dep(val core: String? = null, val complier: String? = null)

object Deps : DepGroup() {

    object support : DepGroup() {

        object v7 : Dep("com.android.support:support-annotations:${Vers.support_version}")

        object annotation : Dep ("com.android.support:appcompat-v7:${Vers.support_version}")
    }
}

//object Deps : HashMap<String, Dep>() {
//
//    val support = Dep(
//            core = listOf(
//                    "com.android.support:support-annotations:${Vers.support_version}",
//                    "com.android.support:appcompat-v7:$${Vers.support_version}",
//                    "android.arch.lifecycle:extensions:1.1.1",
//                    "com.android.support.constraint:constraint-layout:1.1.3"
//            )
//    )
//
//    val view = Dep(
//            core = listOf(
//                    "com.android.support:cardview-v7:${Vers.support_version}",
//                    "com.android.support:recyclerview-v7:${Vers.support_version}",
//                    "com.android.support:design:${Vers.support_version}",
//                    "com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.46",
//                    "com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar"
//            )
//    )
//
//    val reactive = Dep(
//            core = listOf(
//                    "io.reactivex.rxjava2:rxjava:${Vers.rxJava_version}",
//                    "io.reactivex.rxjava2:rxkotlin:${Vers.rxKt_version}",
//                    "io.reactivex.rxjava2:rxandroid:2.1.0",
//                    "com.github.tbruyelle:rxpermissions:0.10.2"
//            )
//    )
//
//    val coroutine = Dep(
//            core = listOf(
//                    "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Vers.coroutine_version}",
//                    "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Vers.coroutine_version}",
//                    "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:${Vers.coroutine_version}"
//            )
//    )
//
//    val retrofit = Dep(
//            core = listOf(
//                    "com.squareup.retrofit2:retrofit:${Vers.retrofit_version}",
//                    "com.squareup.retrofit2:converter-gson:${Vers.retrofit_version}",
//                    "com.squareup.retrofit2:converter-moshi:${Vers.retrofit_version}",
//                    "com.squareup.retrofit2:adapter-rxjava2:${Vers.retrofit_version}"
//            )
//    )
//
//    val glide = Dep(
//            core = listOf(
//                    "com.github.bumptech.glide:glide:${Vers.glide_version}",
//                    "com.github.bumptech.glide:recyclerview-integration:${Vers.glide_version}",
//                    "com.github.bumptech.glide:okhttp3-integration:${Vers.glide_version}",
//                    "jp.wasabeef:glide-transformations:4.1.0"
//            ),
//            compiler = listOf("com.github.bumptech.glide:compiler:${Vers.glide_version}")
//    )
//
//    val tools = Dep(
//            listOf(
//                    "com.jakewharton.timber:timber:4.7.0",
//                    "com.gyf.immersionbar:immersionbar:2.3.2",
//                    "com.squareup.moshi:moshi-kotlin:${Vers.moshi_version}"
//            ),
//            listOf("com.squareup.moshi:moshi-kotlin-codegen:${Vers.moshi_version}")
//
//    )
//}

val methods = arrayOf("implementation", "compileOnly", "annotationProcessor", "kapt", "api")

fun addDep(project: Project, key: Any?, dep: Any?) {
    val cls = Deps::class
    cls.declaredMemberProperties.forEach {
        it.name

    }
    when (key) {
        "implementation" ->
            project.dependencies {
                println("implementation:$dep")
                //implementation(dep)
            }
        "compileOnly" ->
            project.dependencies {
                //compileOnly dep
            }
        "api" ->
            project.dependencies {
                //api dep
            }
        "annotationProcessor" ->
            project.dependencies {
                //annotationProcessor dep
            }
        "kapt" ->
            project.dependencies {
                println("kapt:$dep")
                //kapt dep
            }
    }
}


fun implementationAndKapt(project: Project, dependencies: Any) {
    if (dependencies is Class<*>) {
        if (dependencies.superclass == DepGroup::class.java) {
            dependencies.declaredClasses.forEach {
                println("declaredClasses: $it")
                if (it.superclass == Dep::class.java) {
                    val dep = it.getField("INSTANCE").get(null) as Dep
                    project.dependencies {

//                        LinkerServices.Implementation(dep.core)
//                        kapt(dep.complier)
                    }
                }else if (it.superclass == DepGroup::class.java) {
                    implementationAndKapt(project, it)
                }
            }
        }
    }
//    val kCls = dependencies::class
//    kCls.superclasses.forEach {
//        println("super: $it")
//    }
//    //if (dependencies is DepGroup) {
//    val cls = dependencies.javaClass
//    println("cls:${cls.typeName}")
//    val superCls = DepGroup::class.java
//    println("super:${cls.genericSuperclass.typeName}")
//
//    cls.declaredClasses.forEach {
//        println("declaredClasses:${it.name}")
//    }

}