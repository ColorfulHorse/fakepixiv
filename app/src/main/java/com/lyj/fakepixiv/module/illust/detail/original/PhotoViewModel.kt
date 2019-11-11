package com.lyj.fakepixiv.module.illust.detail.original

import android.graphics.Bitmap
import android.os.Build
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.network.LoadState

/**
 * @author green sun
 *
 * @date 2019/11/11
 *
 * @desc
 */
class PhotoViewModel : BaseViewModel() {
    val loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    var cover: Bitmap? = null

    fun save() {

    }
}