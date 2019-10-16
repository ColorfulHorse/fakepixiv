package com.lyj.fakepixiv.module.illust.detail

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.utils.screenHeight
import com.lyj.fakepixiv.databinding.DialogDetailBottomBinding
import com.lyj.fakepixiv.module.common.DetailViewModel
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

    companion object {
        fun newInstance(): AboutDialogFragment {
            return AboutDialogFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_detail_bottom, container, false)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, screenHeight()*2/3)
        rootView.layoutParams = lp
        mBinding = DataBindingUtil.bind(rootView)
        mBinding?.setLifecycleOwner(this)
        detailViewModel?.let {
            mBinding?.vm = it
        }

        mBinding?.let { binding ->
            with(binding) {
                detailViewModel?.let { vm ->
                    context?.let {
                        val descFooter = DescFooter(it, vm.illust, descContainer)
                        if (vm is IllustDetailViewModel) {
                            val seriesItem = SeriesItem(it, vm.seriesItemViewModel)
                        }
                        val userFooter = UserFooter(it, vm.userFooterViewModel, userContainer)
                        val commentFooter = CommentFooter(it, vm.commentFooterViewModel, commentContainer)
                    }
//                    vm.illust.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
//                        caption.setVariable(BR.data, vm.illust.get())
//                    })
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    return@setOnKeyListener true
                }
            }
            false
        }
        val view = dialog.window.findViewById<View>(android.support.design.R.id.design_bottom_sheet)
        (view as ViewGroup).let {
            it.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
            it.isFocusable = true
            it.isFocusableInTouchMode = true
        }
        bottomSheetBehavior = BottomSheetBehavior.from(view)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
//        mBinding?.scrollView?.post {
//            mBinding?.scrollView?.scrollTo(0, 0)
//        }
        val parent = view.parent as View
        parent.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        detailViewModel?.let {
            it.userFooterViewModel.load()
            it.commentFooterViewModel.load()
        }

    }
}