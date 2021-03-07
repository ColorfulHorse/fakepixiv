package com.lyj.fakepixiv.module.login.filter

import androidx.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.LoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class FilterViewModel : BaseViewModel() {

    var loginState: ObservableField<LoadState> = ObservableField(LoadState.Loading)

    var code = ""
    var via = ""

    fun login(code: String, via: String) {
        this.code = code
        this.via = via
        loginState.set(LoadState.Loading)
        launch {
            flow { emit(UserRepository.instance.loginV2(code, UserRepository.instance.code_verifier, via == "signup")) }
                    .flowOn(Dispatchers.IO)
                    .catch {
                        loginState.set(LoadState.Failed(it))
                    }
                    .collect {
                        loginState.set(LoadState.Succeed)
                    }
        }
    }

    fun retry() {
        login(code, via)
    }

}