package com.lyj.fakepivix.module.main.illust

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.utils.screenHeight

/**
 * @author greensun
 *
 * @date 2019/6/25
 *
 * @desc 相关作品dialog
 */
class RelatedDialogFragment : DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.dialog_related, null)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lp = dialog.window.attributes as WindowManager.LayoutParams
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = screenHeight()/3
        lp.dimAmount = 0.2f
//        lp.windowAnimations = R.style
    }
}