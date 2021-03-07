package com.lyj.fakepixiv.module.setting.account

import android.text.InputType
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.response.AccountState
import com.lyj.fakepixiv.app.data.model.response.EditAccountResp
import com.lyj.fakepixiv.app.data.model.response.LoginData
import com.lyj.fakepixiv.app.data.model.response.ValidationErrors
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * @author green sun
 *
 * @date 2019/11/20
 *
 * @desc
 */
class AccountViewModel : BaseViewModel() {

    val loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    val updateState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    val data = ObservableField<AccountState>()
    val validationErrors = ObservableField<ValidationErrors>()

    @get:Bindable
    var email: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }
    @get:Bindable
    var id: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
    @get:Bindable
    var password: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }

    // 原始id
    lateinit var orgId: String

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        UserRepository.instance
                .loginData?.let {
            email = it.user.mail_address
            id = it.user.account
            orgId = it.user.account
        }
        loadData()
    }

    fun loadData() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                UserRepository.instance
                        .service
                        .getAccountState()
            }
            data.set(resp.user_state)
            loadState.set(LoadState.Succeed)
        }
    }

    /**
     * 更新账户信息
     */
    fun update() {
        SPUtil.getLoginData()?.let { data ->
            if (id != orgId) {
                Router.getTopFragment()?.context?.let {
                    MaterialDialog(it).show {
                        title(R.string.settings_pixiv_id_title)
                        message(R.string.settings_pixiv_id_description)
                        negativeButton(R.string.settings_pixiv_id_dont_change) {
                            id = orgId
                        }
                        positiveButton(R.string.settings_pixiv_id_change) {
                            cancel()
                            checkProvisional(data)
                        }
                    }
                }
            }else {
                checkProvisional(data)
            }
        }
    }

    /**
     * 判断是否临时用户，非临时用户需要输入初始密码
     */
    private fun checkProvisional(data: LoginData) {
        Router.getTopFragment()?.context?.let {
            if (!data.provisional) {
                MaterialDialog(it).show {
                    title(R.string.input_password)
                    message(R.string.msg_input_password)
                    input(hintRes = R.string.current_password, inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    positiveButton(R.string.confirm) {
                        editAccount(getInputField().text.toString())
                    }
                }
            } else {
                editAccount(data.user.password)
            }
        }
    }

    /**
     * 修改账号信息
     */
    private fun editAccount(oldPassword: String = "") {
        validationErrors.set(null)
        launch(CoroutineExceptionHandler { _, err ->
            if (err is HttpException) {
                err.response()?.errorBody()?.let {
                    val resp = JsonUtil.json2Bean<EditAccountResp>(it.string())
                    resp?.let {
                        val errors = resp.body.validation_errors
                        validationErrors.set(errors)
                        var msg = resp.message
                        if (errors.old_password.isNotBlank()) {
                            msg = errors.old_password
                        }
                        ToastUtil.showToast(msg)
                    }
                }
            }
            updateState.set(LoadState.Failed(err))
        }) {
            updateState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                UserRepository.instance
                        .service
                        .editAccount(email, id, oldPassword, password)
            }
            if (resp.body.is_succeed) {
                resp.body.oauth?.let { data ->
                    with(data) {
                        UserRepository.instance.loginData?.let {
                            it.user.account = id
                            it.refresh_token = refresh_token
                            it.access_token = access_token
                            it.user.password = password
                            it.user.mail_address = email
                            it.provisional = false
                            SPUtil.saveLoginData(it)
                        }
                        updateState.set(LoadState.Succeed)
                        Router.getTopFragment()?.pop()
                    }
                }
            }
        }
    }
}