package com.lyj.fakepivix.module.common

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.base.BaseDialogFragment
import com.lyj.fakepivix.app.data.model.response.Tag
import com.lyj.fakepivix.app.databinding.onPropertyChangedCallback
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.utils.ToastUtil
import com.lyj.fakepivix.app.utils.bindState
import com.lyj.fakepivix.databinding.DialogStarBinding
import com.lyj.fakepivix.databinding.ItemStarTagBinding

/**
 * @author greensun
 *
 * @date 2019/9/16
 *
 * @desc
 */
class StarDialog : BaseDialogFragment<DialogStarBinding, StarDialogViewModel>() {

    override var mViewModel: StarDialogViewModel = StarDialogViewModel()

    companion object {
        fun newInstance() = StarDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.full_screen_dialog)
    }

    override fun init(savedInstanceState: Bundle?) {
        val lp = dialog.window.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window.attributes = lp
        with(mBinding) {
            val adapter = object : BaseBindingAdapter<Tag, ItemStarTagBinding>(R.layout.item_star_tag, mViewModel.tags, BR.data) {
                override fun convert(helper: BaseBindingViewHolder<ItemStarTagBinding>, item: Tag) {
                    super.convert(helper, item)
                }
            }
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter.bindState(mViewModel.loadState) {
                mViewModel.load()
            }
            adapter.bindToRecyclerView(recyclerView)
            close.setOnClickListener {
                dismiss()
            }
        }
        mViewModel.starState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
            when(mViewModel.starState.get()) {
                is LoadState.Succeed -> {
                    dismiss()
                }

                is LoadState.Failed -> {
                    if (mViewModel.data.is_bookmarked) {
                        ToastUtil.showToast(R.string.unBookmark_failed)
                    }else {
                        ToastUtil.showToast(R.string.bookmark_failed)
                    }
                }
            }
        })
        mViewModel.load()
    }

    override fun bindLayout(): Int = R.layout.dialog_star
}