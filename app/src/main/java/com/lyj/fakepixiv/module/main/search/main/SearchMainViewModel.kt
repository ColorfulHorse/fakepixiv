package com.lyj.fakepixiv.module.main.search.main

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Tag
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.SearchRepository
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.module.common.IllustListViewModel
import com.lyj.fakepixiv.module.common.UserListViewModel
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 搜索
 */
class SearchMainViewModel constructor(@IllustCategory c: String = IllustCategory.ILLUST) : BaseViewModel() {

    var category: String = IllustCategory.ILLUST
    set(value) {
        field = value
        if (value == IllustCategory.OTHER) {
            showUser.set(true)
        }else {
            showUser.set(false)
        }
    }

    var newVm: IllustListViewModel
    var polularVm: IllustListViewModel
    var descVm: IllustListViewModel
    var subVmList: List<IllustListViewModel>
    var userViewModel: UserListViewModel

    // 是否显示搜索列表
    var showSearch = ObservableField(false)
    // 是否显示补全列表
    var showComplete = ObservableField(false)
    // 是否显示用户列表
    var showUser = ObservableField(false)
    lateinit var action: Emitter<String>
    private var lastCompleteTask: Disposable? = null

    // 输入空格显示历史记录，非空格自动补全
    @get:Bindable
    var inputText = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.inputText)
            if (showSearch.get() == false) {
                if (field.isNotEmpty()) {
                    val list = field.trim().split(Pattern.compile("\\s+"))
                    words.clear()
                    words.addAll(list)
                    if (field.endsWith(" ")) {
                        showComplete.set(false)
                    }else {
                        val next = list.last()
                        showComplete.set(true)
                        action.onNext(next)
                    }
                }else{
                    showComplete.set(false)
                }
            }
        }

    // 真正用来搜索的关键字字符串
    var keyword = ""
    // 用于搜索的关键字列表
    var words = ObservableArrayList<String>()
    // 开始时间
    var start = ""
    // 结束时间
    var end = ""

    // 策略
    var strategy = Constant.Request.KEY_SEARCH_PARTIAL

    var historyList = ObservableArrayList<String>()
    // 自动补全标签
    var tags = ObservableArrayList<Tag>()


    init {
        this.category = c
        newVm = IllustListViewModel {
            IllustRepository.instance
                    .searchIllust(category, keyword, false, start, end, strategy)
        }
        polularVm = IllustListViewModel {
            IllustRepository.instance
                    .searchPopularIllust(category, keyword, start, end, strategy)
        }
        descVm = IllustListViewModel {
            IllustRepository.instance
                    .searchIllust(category, keyword, true, start, end, strategy)
        }
        subVmList = listOf(newVm, polularVm, descVm)

        userViewModel = UserListViewModel {
            UserRepository.instance
                    .searchUser(keyword)
        }
        this + userViewModel

        // 自动补全
        val disposable = Observable.create<String> {
               action = it
        }
                .debounce(200, TimeUnit.MILLISECONDS)
                .flatMap {
                    val sub = SearchRepository.instance
                            .searchAutoComplete(it)
                            .doOnSubscribe { disposable ->
                                lastCompleteTask?.dispose()
                                lastCompleteTask = disposable
                            }
                    sub
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

    /**
     * 拼接关键词列表搜索
     */
    fun search() {
        keyword = words.reduce { s1, s2 -> "$s1 $s2" }
        historyList.add(keyword)
        SPUtil.saveSearchHistory(keyword)
        showSearch.set(true)
        if (category == IllustCategory.OTHER) {
            showUser.set(true)
            userViewModel.load()
        }else {
            subVmList.forEach {
                if (it.lazyCreated) {
                    it.load()
                }
            }
        }
    }

    fun clearHistory() {
        historyList.clear()
        SPUtil.removeAllSearchHistory()
    }

}