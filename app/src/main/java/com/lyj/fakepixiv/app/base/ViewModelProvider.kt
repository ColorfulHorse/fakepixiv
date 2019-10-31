package com.lyj.fakepixiv.app.base

/**
 * @author greensun
 *
 * @date 2019/7/23
 *
 * @desc
 */
object ViewModelProvider {
    val data = hashMapOf<Int, BaseViewModel>()

    operator fun <T: BaseViewModel> get(key: Any): T {
        return data[key.hashCode()] as T
    }

    operator fun <T : BaseViewModel> set(hash: Int, vm: T) {
        data[hash] = vm
    }

    operator fun minus(hash: Int) {
        data.remove(hash)
    }

}