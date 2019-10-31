package com.lyj.fakepixiv.module.novel

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.databinding.bindAction
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.DialogNovelBinding
import com.lyj.fakepixiv.module.common.adapter.IllustTagAdapter

/**
 * @author greensun
 *
 * @date 2019/8/9
 *
 * @desc
 */
class NovelDialogFragment : DialogFragment() {
    private lateinit var mViewModel: NovelDialogViewModel
    private lateinit var mBinding: DialogNovelBinding
    private var key = -1
    private var position = -1

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_KEY = "EXTRA_KEY"
        fun newInstance(position: Int, key: Int): NovelDialogFragment {
            return NovelDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putInt(EXTRA_KEY, key)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_novel, null, false)
        arguments?.let {
            position = it.getInt(EXTRA_POSITION, -1)
            key = it.getInt(EXTRA_KEY, -1)
            mViewModel = NovelDialogViewModel(key, position)
            lifecycle.addObserver(mViewModel)
            mBinding.setVariable(BR.vm, mViewModel)
        }
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(mBinding) {
            val adapter = IllustTagAdapter(IllustCategory.NOVEL, R.layout.item_tag, mViewModel.data.getTranslateTags())
            adapter.bindToRecyclerView(recyclerView)
            recyclerView.layoutManager = ChipsLayoutManager.newBuilder(ApplicationLike.context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .build()
            recyclerView.addItemDecoration(SpacingItemDecoration(1.dp2px(), 6.dp2px()))
            likeButton.bindAction(mViewModel.data)
            closeContainer.setOnClickListener {
                dismiss()
            }

            userContainer.setOnClickListener {
                dismiss()
                Router.goUserDetail(mViewModel.data.user)
            }

            readContainer.setOnClickListener {
                dismiss()
                Router.goNovelDetail(key, position)
            }

            seriesContainer.setOnClickListener {
                Router.goNovelSeries(mViewModel.data.series?.id.toString())
            }

            likes.setOnClickListener {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window.setGravity(Gravity.CENTER)
    }
}