package com.lyj.fakepivix.module.illust.bookmark

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.flyco.tablayout.utils.FragmentChangeManager
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.base.BaseDialogFragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.EXTRA_CATEGORY
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.BookmarkTag
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.app.utils.bindState
import com.lyj.fakepivix.databinding.DialogStarFilterBinding

/**
 * @author greensun
 *
 * @date 2019/9/16
 *
 * @desc  收藏筛选
 */
class FilterDialog : BaseDialogFragment<DialogStarFilterBinding, FilterViewModel>() {

    override var mViewModel: FilterViewModel = FilterViewModel()

    companion object {
        fun newInstance(@IllustCategory category: String) = FilterDialog().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            val category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
            mViewModel.category = category
        }
        with(mBinding) {
            val publicAdapter = BaseBindingAdapter<BookmarkTag, ViewDataBinding>(R.layout.item_star_filter, mViewModel.publicTags, BR.data)
            val privateAdapter = BaseBindingAdapter<BookmarkTag, ViewDataBinding>(R.layout.item_star_filter, mViewModel.privateTags, BR.data)
            rvPublic.layoutManager = LinearLayoutManager(context)
            rvPrivate.layoutManager = LinearLayoutManager(context)
            rvPublic.adapter = publicAdapter
            rvPrivate.adapter = privateAdapter
            publicAdapter.bindState(mViewModel.publicLoadState, errorRes = R.layout.layout_error_small)
            privateAdapter.bindState(mViewModel.privateLoadState, errorRes = R.layout.layout_error_small)
            segmentLayout.setTabData(
                    arrayListOf<CustomTabEntity>(
                            TabBean(title = getString(R.string.tab_public)),
                            TabBean(title = getString(R.string.tab_private))
                    ))
            segmentLayout.currentTab = 0
        }
    }

    override fun bindLayout(): Int = R.layout.dialog_star_filter
}