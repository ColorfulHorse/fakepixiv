package com.lyj.fakepivix.module.common

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.Tag
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author greensun
 *
 * @date 2019/9/16
 *
 * @desc
 */
class StarDialogViewModel : BaseViewModel<IModel?>() {
    override val mModel: IModel? = null

    var loadState: ObservableField<LoadState> = ObservableField(LoadState.Idle)
    var starState: ObservableField<LoadState> = ObservableField(LoadState.Idle)

    @get: Bindable
    var data = Illust()
        set(value) {
            field = value
            notifyPropertyChanged(BR.illust)
        }

    @get: Bindable
    var pri = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.pri)
        }

    val tags = ObservableArrayList<Tag>()


    fun load() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            tags.clear()
            val resp = withContext(Dispatchers.IO) {
                IllustRepository.instance
                        .getBookMarkDetail(data.id.toString(), data.type)
            }
            if (resp.bookmark_detail.tags.isEmpty()) {
                throw ApiException(ApiException.CODE_EMPTY_DATA)
            }
            tags.addAll(resp.bookmark_detail.tags)
            pri = resp.bookmark_detail.restrict == Restrict.PRIVATE
            loadState.set(LoadState.Succeed)
        }
    }

    fun star(edit: Boolean) {
        val disposable = IllustRepository.instance.star(data, starState, tags.filter { it.is_registered }.map { it.name }, if (pri) Restrict.PRIVATE else Restrict.PUBLIC, edit)
        disposable?.let {
            addDisposable(it)
        }
    }
}