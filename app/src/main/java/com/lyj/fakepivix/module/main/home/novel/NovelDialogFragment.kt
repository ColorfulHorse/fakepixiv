package com.lyj.fakepivix.module.main.home.novel

import android.arch.lifecycle.LifecycleObserver
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.databinding.DialogNovelBinding
import com.lyj.fakepivix.module.main.illust.IllustDetailRootFragment

/**
 * @author greensun
 *
 * @date 2019/8/9
 *
 * @desc
 */
class NovelDialogFragment : DialogFragment() {
    private var mViewModel: NovelDialogViewModel? = null
    private lateinit var mBinding: DialogNovelBinding

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
//        fun newInstance(position: Int, data: List<Illust>): NovelDialogFragment {
//            return NovelDialogFragment().apply {
//                this.data = data
//                arguments = Bundle().apply {
//                    putInt(EXTRA_POSITION, position)
//                }
//            }
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_novel, null, false)
        mViewModel?.let {
            lifecycle.addObserver(mViewModel as LifecycleObserver)
            mBinding.setVariable(BR.vm, mViewModel)
        }
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}