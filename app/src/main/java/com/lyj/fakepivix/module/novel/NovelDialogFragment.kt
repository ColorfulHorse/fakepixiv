package com.lyj.fakepivix.module.novel

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.data.model.response.Tag
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.databinding.DialogNovelBinding
import com.lyj.fakepivix.databinding.ItemTagBinding
import com.lyj.fakepivix.widget.FlowLayoutManager

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
            val adapter = BaseBindingAdapter<Tag, ItemTagBinding>(R.layout.item_tag, mViewModel.data.getTranslateTags(), BR.data)
            adapter.bindToRecyclerView(recyclerView)
            recyclerView.layoutManager = FlowLayoutManager()
            likeContainer.setOnClickListener {

            }
            closeContainer.setOnClickListener {
                this@NovelDialogFragment.dismiss()
            }

            readContainer.setOnClickListener {
                dismiss()
                Router.goNovelDetail(key, position)
            }

            seriesContainer.setOnClickListener {

            }

            likes.setOnClickListener {

            }

            avatar.setOnClickListener {

            }

            nickName.setOnClickListener {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window.setGravity(Gravity.CENTER)
    }
}