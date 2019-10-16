package com.lyj.fakepixiv.module.common

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.base.BaseDialogFragment
import com.lyj.fakepixiv.app.data.model.response.Tag
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.ToastUtil
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.databinding.DialogStarBinding
import com.lyj.fakepixiv.databinding.ItemStarTagBinding

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