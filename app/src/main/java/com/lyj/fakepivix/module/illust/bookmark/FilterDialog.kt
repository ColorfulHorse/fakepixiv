package com.lyj.fakepivix.module.illust.bookmark

import android.os.Bundle
import android.os.Handler
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.utils.FragmentChangeManager
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BaseDialogFragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.DialogStarFilterBinding

/**
 * @author greensun
 *
 * @date 2019/9/16
 *
 * @desc
 */
class FilterDialog : BaseDialogFragment<DialogStarFilterBinding, BaseViewModel<IModel?>?>() {

    override var mViewModel: BaseViewModel<IModel?>? = null

    companion object {
        fun newInstance() = FilterDialog()
    }

    override fun init(savedInstanceState: Bundle?) {
        mBinding.segmentLayout.setTabData(
                arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_public)),
                TabBean(title = getString(R.string.tab_private))
        ))
    }

    override fun bindLayout(): Int = R.layout.dialog_star_filter
}