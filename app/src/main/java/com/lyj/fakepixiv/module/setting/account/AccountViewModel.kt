package com.lyj.fakepixiv.module.setting.account

import android.text.InputType
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.response.AccountState
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.AppManager
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.app.utils.ToastUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

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

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        UserRepository.instance
                .loginData?.let {
            email = it.user.mail_address
            id = it.user.account
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

    fun update() {
        SPUtil.getLoginData()?.let { data ->
            if (!data.provisional) {
                Router.getTopFragment()?.context?.let {
                    MaterialDialog(it).show {
                        title(R.string.input_password)
                        message(R.string.msg_input_password)
                        input(hintRes = R.string.current_password, inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        positiveButton(R.string.confirm) {
                            editAccount(getInputField().text.toString())
                        }
                    }
                }
            }else {
                editAccount(data.user.password)
            }
        }
    }

    /**
     * 修改账号信息
     */
    private fun editAccount(oldPassword: String = "") {
        launch(CoroutineExceptionHandler { _, err ->
            Timber.e(err)
            updateState.set(LoadState.Failed(err))
            if (err is ApiException) {
                ToastUtil.showToast(R.string.error_password)
            }else {
                ToastUtil.showToast(R.string.loading_error)
            }
        }) {
            updateState.set(LoadState.Loading)
            withContext(Dispatchers.IO) {
                UserRepository.instance
                        .service
                        .editAccount(email, id, oldPassword, password)
            }
            updateState.set(LoadState.Succeed)
        }
    }
}