package com.lyj.fakepivix.module.main.search.main

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.v4.app.Fragment
import android.util.Log
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Tag
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.SearchRepository
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.app.utils.SPUtil
import com.lyj.fakepivix.module.main.common.IllustListViewModel
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import me.yokeyword.fragmentation.SupportHelper
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.properties.Delegates

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class SearchMainViewModel(@IllustCategory var category: String = IllustCategory.ILLUST) : BaseViewModel<ISearchMainModel>() {

    override var mModel: ISearchMainModel = SearchMainModel()
    var newVm: IllustListViewModel
    var polularVm: IllustListViewModel
    var descVm: IllustListViewModel

    var showSearch = ObservableField(false)
    var showComplete = ObservableField(false)
    lateinit var action: Emitter<String>
    private var lastCompleteTask: Disposable? = null

    @get:Bindable
    var keyword = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.keyword)
            val keyword = value.trim()
            showComplete.set(keyword.length > 1)
            if (keyword.length > 1) {
                action.onNext(keyword)
            }
        }
    // 开始时间
    var start = ""
    // 结束时间
    var end = ""
    // 策略
    var strategy = Constant.Request.KEY_SEARCH_PARTIAL

    var historyList = ObservableArrayList<String>()
    var words = ObservableArrayList<String>()
    // 自动补全标签
    var tags = ObservableArrayList<Tag>()

    init {
        newVm = IllustListViewModel(category) {
            IllustRepository.instance
                    .searchIllust(category, keyword, true, start, end, strategy)
        }
        polularVm = IllustListViewModel(category) {
            IllustRepository.instance
                    .searchPopularIllust(category, keyword, start, end, strategy)
        }
        descVm = IllustListViewModel(category) {
            IllustRepository.instance
                    .searchIllust(category, keyword, false, start, end, strategy)
        }

        // 自动补全
        val disposable = Observable.create<String> {
               action = it
        }
                .debounce(200, TimeUnit.MILLISECONDS)
                .flatMap {
                    val sub = SearchRepository.instance
                            .searchAutoComplete(it)
                    sub
                }
                .doOnSubscribe {
                    lastCompleteTask?.dispose()
                    lastCompleteTask = it
                }
                .subscribeBy(onNext = {
                    tags.clear()
                    tags.addAll(it)
                }, onError = {

                })
        addDisposable(disposable)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        val words = SPUtil.getSearchHistory()
        if (words.isNotEmpty()) {
            historyList.addAll(words)
        }
    }

    fun search() {
        showSearch.set(true)
        SPUtil.saveSearchHistory(keyword)
        words.clear()
        words.addAll(keyword.split(Pattern.compile("\\s+")))
    }

}