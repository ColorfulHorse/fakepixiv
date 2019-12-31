package com.lyj.fakepixiv.module.illust.detail.original

import android.graphics.Bitmap
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.databinding.Dynamic
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.FileUtil
import com.lyj.fakepixiv.app.utils.ToastUtil
import com.lyj.fakepixiv.app.utils.ioTask
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @author green sun
 *
 * @date 2019/11/11
 *
 * @desc
 */
class PhotoViewModel(val data: Illust) : BaseViewModel() {
    val loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var file: File? = null

    @get:Bindable
    var showToolbar by Dynamic(false, BR.showToolbar)

    fun showBar() {
        showToolbar = !showToolbar
    }

    fun save() {
        file?.let {
            launch(CoroutineExceptionHandler { _, _ ->
                ToastUtil.showToast(R.string.save_failed)
            }) {
                val key = "${data.id}_${data.image_urls.original.substringAfterLast("/")}"
                withContext(Dispatchers.IO) {
                    FileUtil.saveImage(it, key)
                }
                ToastUtil.showToast(R.string.save_succeed)
            }
        }
    }
}