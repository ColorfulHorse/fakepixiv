package com.lyj.fakepivix.module.main.search.main

import android.arch.lifecycle.LifecycleOwner
import android.databinding.Bindable
import android.support.v4.app.Fragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.module.main.common.IllustListViewModel
import me.yokeyword.fragmentation.SupportHelper

/**
 * @author greensun
 *
 * @date 2019/3/13
 *
 * @desc 登录
 */
class SearchMainViewModel(@IllustCategory var category: String = IllustCategory.ILLUST) : BaseViewModel<ISearchMainModel>() {

    override var mModel: ISearchMainModel = SearchMainModel()
    lateinit var newVm: IllustListViewModel
    lateinit var polularVm: IllustListViewModel
    lateinit var DescVm: IllustListViewModel


    var kayword = ""
    // 开始时间
    var start = ""
    // 结束时间
    var end = ""

    var strategy = ""

    init {
        newVm = IllustListViewModel(category) {
            IllustRepository.instance
                    .searchIllust(category, kayword, true, start, end)
        }
        polularVm = IllustListViewModel(category) {
            IllustRepository.instance
                    .searchPopularIllust(category, kayword, start, end)
        }
        DescVm = IllustListViewModel(category) {
            IllustRepository.instance
                    .searchIllust(category, kayword, false, start, end)
        }
    }

}