package com.lyj.fakepixiv.module.illust.bookmark

import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.WindowManager
import com.flyco.tablayout.listener.CustomTabEntity
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.base.BaseDialogFragment
import com.lyj.fakepixiv.app.constant.EXTRA_CATEGORY
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.BookmarkTag
import com.lyj.fakepixiv.app.entity.TabBean
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.databinding.DialogStarFilterBinding

/**
 * @author greensun
 *
 * @date 2019/9/16
 *
 * @desc  收藏筛选
 */
class FilterDialog : BaseDialogFragment<DialogStarFilterBinding, FilterViewModel>() {

    override var mViewModel: FilterViewModel = FilterViewModel()

    var onSelect: ((String, String) -> Unit)? = null

    companion object {
        const val EXTRA_PUBLIC_TAG = "EXTRA_PUBLIC_TAG"
        const val EXTRA_PRIVATE_TAG = "EXTRA_PRIVATE_TAG"
        const val EXTRA_RESTRICT = "EXTRA_RESTRICT"
        fun newInstance(@IllustCategory category: String, @Restrict restrict: String,
                        publicTag: String, privateTag: String, onSelect: (String, String) -> Unit) = FilterDialog().apply {
            this.onSelect = onSelect
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
                putString(EXTRA_PUBLIC_TAG, publicTag)
                putString(EXTRA_PRIVATE_TAG, privateTag)
                putString(EXTRA_RESTRICT, restrict)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.full_screen_dialog)
    }

    override fun init(savedInstanceState: Bundle?) {
        val lp = dialog?.window?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        lp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = lp
        arguments?.let {
            val category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
            var publicTag = it.getString(EXTRA_PUBLIC_TAG, "")
            var privateTag = it.getString(EXTRA_PRIVATE_TAG, "")
            var restrict = it.getString(EXTRA_RESTRICT, Restrict.PUBLIC)
            if (publicTag.isEmpty()) {
                publicTag = getString(R.string.all)
            }
            if (privateTag.isEmpty()) {
                privateTag = getString(R.string.all)
            }
            mViewModel.category = category
            mViewModel.publicTag = publicTag
            mViewModel.privateTag = privateTag
            mViewModel.isPub = restrict == Restrict.PUBLIC
        }
        with(mBinding) {
            val publicAdapter = BaseBindingAdapter<BookmarkTag, ViewDataBinding>(R.layout.item_star_filter, mViewModel.publicTags, BR.data)
            val privateAdapter = BaseBindingAdapter<BookmarkTag, ViewDataBinding>(R.layout.item_star_filter, mViewModel.privateTags, BR.data)
            rvPublic.layoutManager = LinearLayoutManager(context)
            rvPrivate.layoutManager = LinearLayoutManager(context)
            rvPublic.adapter = publicAdapter
            rvPrivate.adapter = privateAdapter
            publicAdapter.bindState(mViewModel.publicLoadState, errorRes = R.layout.layout_error_small) {
                mViewModel.loadPublic()
            }
            privateAdapter.bindState(mViewModel.privateLoadState, errorRes = R.layout.layout_error_small) {
                mViewModel.loadPrivate()
            }
            publicAdapter.setOnItemClickListener { baseQuickAdapter, view, position ->
                val tag = mViewModel.publicTags[position]
                if (!tag.selected) {
                    mViewModel.publicTags.forEach { it.selected = false }
                    tag.selected = true
                    onSelect?.invoke(Restrict.PUBLIC, if (position == 0) "" else tag.name)
                    dismiss()
                }
            }

            privateAdapter.setOnItemClickListener { baseQuickAdapter, view, position ->
                val tag = mViewModel.privateTags[position]
                if (!tag.selected) {
                    mViewModel.privateTags.forEach { it.selected = false }
                    tag.selected = true
                    onSelect?.invoke(Restrict.PRIVATE, if (position == 0) "" else tag.name)
                    dismiss()
                }
            }
            segmentLayout.setTabData(
                    arrayListOf<CustomTabEntity>(
                            TabBean(title = getString(R.string.tab_public)),
                            TabBean(title = getString(R.string.tab_private))
                    ))
            if (mViewModel.isPub) {
                segmentLayout.currentTab = 0
                mViewModel.loadPublic()
            }else {
                segmentLayout.currentTab = 1
                mViewModel.loadPrivate()
            }
            closeButton.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun bindLayout(): Int = R.layout.dialog_star_filter
}