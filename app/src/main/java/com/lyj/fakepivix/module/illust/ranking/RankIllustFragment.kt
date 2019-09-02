package com.lyj.fakepivix.module.illust.ranking

import android.os.Bundle
import android.provider.ContactsContract.QuickContact.EXTRA_MODE
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.databinding.CommonList
import com.lyj.fakepivix.module.common.IllustListFragment
import com.lyj.fakepivix.module.common.IllustListViewModel

/**
 * @author greensun
 *
 * @date 2019/9/2
 *
 * @desc
 */
class RankIllustFragment : FragmentationFragment<CommonList, IllustListViewModel?>() {

    override var mViewModel: IllustListViewModel? = null

    companion object {
        fun newInstance() = RankIllustFragment().apply {

        }
    }

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler
}