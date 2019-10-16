package com.lyj.fakepixiv.module.common

import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.model.response.Tag
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
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

    @get: Bindable
    var newTag = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.newTag)
        }

    @get: Bindable
    var activeCount = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.activeCount)
        }

    val tags = ObservableArrayList<Tag>()


    init {
        tags.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<Tag>>() {
            override fun onChanged(sender: ObservableList<Tag>?) {

            }

            override fun onItemRangeRemoved(sender: ObservableList<Tag>?, positionStart: Int, itemCount: Int) {

            }

            override fun onItemRangeMoved(sender: ObservableList<Tag>?, fromPosition: Int, toPosition: Int, itemCount: Int) {

            }

            override fun onItemRangeInserted(sender: ObservableList<Tag>, positionStart: Int, itemCount: Int) {
                sender.takeLast(itemCount).forEach {
                    it.addOnPropertyChangedCallback(onPropertyChangedCallback { _, id ->
                        if (id == BR._registered) {
                            if (it.is_registered) {
                                activeCount ++
                            }else {
                                activeCount --
                            }
                        }
                    })
                }
            }

            override fun onItemRangeChanged(sender: ObservableList<Tag>?, positionStart: Int, itemCount: Int) {

            }

        })
    }

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
            activeCount = tags.filter { it.is_registered }.size
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

    fun add() {
        tags.add(0, Tag(name = newTag).apply {
            is_registered = true
        })
        newTag = ""
    }

}