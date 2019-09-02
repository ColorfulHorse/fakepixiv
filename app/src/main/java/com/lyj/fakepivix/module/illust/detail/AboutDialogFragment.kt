package com.lyj.fakepivix.module.illust.detail

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.utils.screenHeight
import com.lyj.fakepivix.databinding.DialogDetailBottomBinding
import com.lyj.fakepivix.module.common.DetailViewModel

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

    companion object {
        fun newInstance(): AboutDialogFragment {
            return AboutDialogFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_detail_bottom, container, false)
        val binding = DataBindingUtil.bind<DialogDetailBottomBinding>(rootView)
        detailViewModel?.let {
            binding?.vm = it
        }

        binding?.let {
            with(binding) {
                detailViewModel?.let { vm ->
                    context?.let {
                        val descFooter = DescFooter(it, vm.illust, descContainer)
                        val userFooter = UserFooter(it, vm.userFooterViewModel, userContainer)
                        val commentFooter = CommentFooter(it, vm.commentFooterViewModel, commentContainer)
                    }
//                    vm.illust.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
//                        caption.setVariable(BR.data, vm.illust.get())
//                    })
                }
                caption.show.setImageResource(R.drawable.action_detail_rev)
                descContainer.tagsLabel.visibility = View.VISIBLE
                descContainer.descLabel.visibility = View.VISIBLE
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
        //dialog.setCanceledOnTouchOutside(false)
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
        bottomSheetBehavior = BottomSheetBehavior.from(view)
        val parent = view.parent as View
        parent.post {
            val h = parent.height
        }
        parent.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        val lp = view.layoutParams as CoordinatorLayout.LayoutParams
        lp.height = screenHeight()*2/3
        detailViewModel?.let {
            it.userFooterViewModel.load()
            it.commentFooterViewModel.load()
        }

    }
}