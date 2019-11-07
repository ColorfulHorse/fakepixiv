import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties

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
    const val support_version = "28.0.0-alpha1"
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
    const val dialogs_version = "3.1.1"
}


@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DepMode(val value: String)

const val IMPLEMENTATION = "implementation"
const val API = "api"
const val COMPILE_ONLY = "compileOnly"
const val DEBUG_IMPLEMENTATION = "debugImplementation"
const val KAPT = "kapt"
const val APT = "annotationProcessor"

open class DepGroup
open class Dep(val core: String? = null, val complier: String? = null)

object Deps : DepGroup() {

    object Support : DepGroup() {

        const val preference = "com.android.support:preference-v7:${Vers.support_version}"

        const val annotation = "com.android.support:support-annotations:${Vers.support_version}"

        const val v7 = "com.android.support:appcompat-v7:${Vers.support_version}"

        const val lifecycle = "android.arch.lifecycle:extensions:1.1.1"

        const val constraintLayout = "com.android.support.constraint:constraint-layout:1.1.3"
    }

    object View : DepGroup() {

        const val cardView = "com.android.support:cardview-v7:${Vers.support_version}"

        const val recyclerView = "com.android.support:recyclerview-v7:${Vers.support_version}"

        const val design = "com.android.support:design:${Vers.support_version}"

        const val adapter = "com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.46"

        const val flyco = "com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar"

//        object MDDialogs : DepGroup() {
//            const val core = "com.afollestad.material-dialogs:core:${Vers.dialogs_version}"
//        }
    }

    object Reactive : DepGroup() {

        const val rxjava = "io.reactivex.rxjava2:rxjava:${Vers.rxJava_version}"

        const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:${Vers.rxKt_version}"

        const val rxandroid = "io.reactivex.rxjava2:rxandroid:2.1.0"

        const val rxpermissions = "com.github.tbruyelle:rxpermissions:0.10.2"

    }

    object Coroutine : DepGroup() {

        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Vers.coroutine_version}"

        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Vers.coroutine_version}"

        const val rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:${Vers.coroutine_version}"

    }

    object Retrofit : DepGroup() {

        const val core = "com.squareup.retrofit2:retrofit:${Vers.retrofit_version}"

        const val gson = "com.squareup.retrofit2:converter-gson:${Vers.retrofit_version}"

        const val moshi = "com.squareup.retrofit2:converter-moshi:${Vers.retrofit_version}"

        const val rxAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Vers.retrofit_version}"
    }

    object Glide : DepGroup() {

        object core : Dep(core = "com.github.bumptech.glide:glide:${Vers.glide_version}",
                complier = "com.github.bumptech.glide:compiler:${Vers.glide_version}")

        const val recyclerview = "com.github.bumptech.glide:recyclerview-integration:${Vers.glide_version}"

        const val okhttp3 = "com.github.bumptech.glide:okhttp3-integration:${Vers.glide_version}"

        const val transformations = "jp.wasabeef:glide-transformations:4.1.0"
    }

    object Tinker : DepGroup() {

        @DepMode(APT)
        const val apt = "com.tencent.tinker:tinker-android-anno:${Vers.tinker_version}"

        @DepMode(COMPILE_ONLY)
        const val anno = "com.tencent.tinker:tinker-android-anno:${Vers.tinker_version}"

        const val lib = "com.tencent.tinker:tinker-android-lib:${Vers.tinker_version}"
    }

    object Tools : DepGroup() {

        const val timber = "com.jakewharton.timber:timber:4.7.0"

        const val immersionbar = "com.gyf.immersionbar:immersionbar:2.3.2"

        const val fragmentation_core = "me.yokeyword:fragmentation-core:${Vers.fragmentation_version}"

        @DepMode(DEBUG_IMPLEMENTATION)
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.0-beta-3"

        object moshiKotlin : Dep("com.squareup.moshi:moshi-kotlin:${Vers.moshi_version}",
                "com.squareup.moshi:moshi-kotlin-codegen:${Vers.moshi_version}")

    }
}


/**
 * 添加依赖组
 */
fun addDeps(project: Project, dependencies: Any) {
    val groupCls = when (dependencies) {
        is DepGroup -> dependencies::class
        is KClass<*> -> dependencies
        else -> {
            addDep(project, dependencies)
            return
        }
    }

    if (groupCls.isSubclassOf(DepGroup::class)) {
        groupCls.nestedClasses.forEach {
            if (it.isSubclassOf(Dep::class)) {
                val dep = it.objectInstance as Dep
                addDep(project, dep)
            } else if (it.isSubclassOf(DepGroup::class)) {
                addDeps(project, it)
            }
        }

        groupCls.memberProperties.forEach {
            val mode = it.findAnnotation<DepMode>()
            val field = it.getter.call()
            if (field is String) {
                addDep(project, field, mode?.value)
            }
        }
    }
}

private fun addDep(project: Project, dep: Any, mode: String? = null) {
    project.dependencies {
        if (dep is Dep) {
            val cls = dep::class
            dep.core?.let { core ->
                val field = cls.memberProperties.find { it.name == "core" }
                val depMode = field?.findAnnotation<DepMode>()
                if (depMode != null) {
                    add(depMode.value, core)
                }else {
                    add(IMPLEMENTATION, core)
                }
            }
            dep.complier?.let { complier ->
                val field = cls.memberProperties.find { it.name == "complier" }
                val depMode = field?.findAnnotation<DepMode>()
                if (depMode != null) {
                    add(depMode.value, complier)
                }else {
                    add(KAPT, complier)
                }
            }
        } else if (dep is String) {
            if (mode != null) {
                println("addDep: mode:$mode==========$dep")
                add(mode, dep)
            }else {
                add(IMPLEMENTATION, dep)
            }
        }
    }
}