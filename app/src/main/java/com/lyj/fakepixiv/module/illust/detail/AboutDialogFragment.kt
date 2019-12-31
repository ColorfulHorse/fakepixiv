package com.lyj.fakepixiv.module.illust.detail

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gyf.immersionbar.ktx.immersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.databinding.DialogDetailBottomBinding
import com.lyj.fakepixiv.module.common.DetailViewModel
import com.lyj.fakepixiv.module.illust.detail.comment.InputBar
import com.lyj.fakepixiv.module.illust.detail.comment.InputViewModel
import com.lyj.fakepixiv.module.illust.detail.items.CommentFooter
import com.lyj.fakepixiv.module.illust.detail.items.DescFooter
import com.lyj.fakepixiv.module.illust.detail.items.SeriesItem
import com.lyj.fakepixiv.module.illust.detail.items.UserFooter

/**
 * @author greensun
 *
 * @date 2019/6/20
 *
 * @desc 作品相关信息dialog
 */
class AboutDialogFragment : BottomSheetDialogFragment() {

    var detailViewModel: DetailViewModel? = null
    var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    var mBinding: DialogDetailBottomBinding? = null
    var inputBar: InputBar? = null

    companion object {
        fun newInstance(): AboutDialogFragment {
            return AboutDialogFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_BottomSheet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_detail_bottom, container, false)
        mBinding = DataBindingUtil.bind(rootView)
        mBinding?.setLifecycleOwner(this)
        detailViewModel?.let {
            mBinding?.vm = it
            lifecycle.addObserver(it)
        }
        mBinding?.let { binding ->
            with(binding) {
                detailViewModel?.let { vm ->
                    inputBar = InputBar(input, vm.commentListViewModel.inputViewModel)
                    context?.let {
                        DescFooter(it, vm.illust, descContainer)
                        if (vm is IllustDetailViewModel) {
                            SeriesItem(it, vm.seriesItemViewModel, seriesContainer)
                        }
                        UserFooter(it, vm.userFooterViewModel, this@AboutDialogFragment, userContainer)
                        CommentFooter(it, vm.commentListViewModel, this@AboutDialogFragment, commentContainer)
                    }
                    descContainer.tagsLabel.visibility = if (vm.illust.getTranslateTags().isEmpty()) View.GONE else View.VISIBLE
                    descContainer.descLabel.visibility = if (vm.illust.caption.isBlank()) View.GONE else View.VISIBLE
                    descContainer.desc.visibility = if (vm.illust.caption.isBlank()) View.GONE else View.VISIBLE
                }
                caption.show.setImageResource(R.drawable.action_detail_rev)
                descContainer.containerCaption.visibility = View.GONE
                caption.show.setOnClickListener {
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        immersionBar {
            keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            keyboardEnable(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.let {
            it.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.action == KeyEvent.ACTION_UP) {
                        if (inputBar?.viewModel?.state != InputViewModel.State.CLOSE) {
                            inputBar?.viewModel?.hide()
                            return@setOnKeyListener true
                        }
                        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                        return@setOnKeyListener true
                    }
                }
                false
            }
            val view = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//        (view as ViewGroup).let {
//            //it.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
//            it.isFocusable = true
//            it.isFocusableInTouchMode = true
//        }
            bottomSheetBehavior = BottomSheetBehavior.from(view)
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED

            val parent = view.parent as View
            parent.setOnClickListener {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
        detailViewModel?.let {
            it.userFooterViewModel.load()
            it.commentListViewModel.load()
        }

    }

    override fun onDestroyView() {
        mBinding?.unbind()
        mBinding = null
        detailViewModel = null
        super.onDestroyView()
    }
}