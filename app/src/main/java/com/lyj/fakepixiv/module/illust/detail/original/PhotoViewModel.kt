package com.lyj.fakepixiv.module.illust.detail.original

import android.graphics.Bitmap
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.FileUtil
import com.lyj.fakepixiv.app.utils.ToastUtil

/**
 * @author green sun
 *
 * @date 2019/11/11
 *
 * @desc
 */
class PhotoViewModel(val data: Illust) : BaseViewModel() {
    val loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var cover: Bitmap? = null

    fun save() {
        cover?.let {
            FileUtil.saveBitmap(it, data.id.toString())
            ToastUtil.showToast(R.string.save_succeed)
        }
    }
}