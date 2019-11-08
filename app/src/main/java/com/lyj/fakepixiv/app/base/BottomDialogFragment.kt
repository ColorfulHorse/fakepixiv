package com.lyj.fakepixiv.app.base

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.Gravity
import android.view.WindowManager
import com.lyj.fakepixiv.R

/**
 * @author greensun
 *
 * @date 2019/6/27
 *
 * @desc
 */
open class BottomDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.bottom_sheet_dialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.let {
            it.setCanceledOnTouchOutside(true)
            it.window?.setBackgroundDrawableResource(R.color.transparent)
            val lp = it.window?.attributes as WindowManager.LayoutParams
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.BOTTOM
            lp.dimAmount = 0f
            lp.windowAnimations = R.style.bottom_sheet_anim
            it.window?.attributes = lp
        }
    }
}