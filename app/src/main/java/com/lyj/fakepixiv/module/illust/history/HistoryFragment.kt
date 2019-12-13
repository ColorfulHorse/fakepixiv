package com.lyj.fakepixiv.module.illust.history

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.model.response.IllustListResp
import com.lyj.fakepixiv.app.data.model.response.IllustResp
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.entity.TabBean
import com.lyj.fakepixiv.databinding.FragmentCommonTabBinding
import com.lyj.fakepixiv.module.common.IllustListFragment
import com.lyj.fakepixiv.module.common.IllustListViewModel
import me.yokeyword.fragmentation.ISupportFragment

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc 浏览历史
 */
class HistoryFragment : BackFragment<FragmentCommonTabBinding, BaseViewModel?>() {
    override var mViewModel: BaseViewModel? = null
    private lateinit var illustViewModel: IllustListViewModel
    private lateinit var novelViewModel: IllustListViewModel

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        mToolbar?.title = getString(R.string.browser_history)
        val illustFragment = IllustListFragment.newInstance(IllustCategory.OTHER,
                IllustListFragment.Config(IllustCategory.OTHER, adapterConfig = {
                    addItemType(Illust.TYPE_ILLUST, R.layout.item_history_illust, BR.data)
                    addItemType(Illust.TYPE_COMIC, R.layout.item_history_illust, BR.data)
                }))
        val novelFragment = IllustListFragment.newInstance(IllustCategory.NOVEL,
                IllustListFragment.Config(IllustCategory.NOVEL, adapterConfig = {
                    addItemType(Illust.TYPE_ILLUST, R.layout.item_history_novel, BR.data)
                    addItemType(Illust.TYPE_NOVEL, R.layout.item_history_novel, BR.data)
                }))
        illustViewModel = HistoryListViewModel(IllustCategory.ILLUST)

        novelViewModel = HistoryListViewModel(IllustCategory.NOVEL)
        illustFragment.mViewModel = illustViewModel
        novelFragment.mViewModel = novelViewModel

        val fragments = arrayOf<ISupportFragment>(illustFragment, novelFragment)
        loadMultipleRootFragment(R.id.fl_container, 0, *fragments)
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_pic)),
                TabBean(title = getString(R.string.tab_novel))
        )
        with(mBinding) {
            segmentLayout.setTabData(tabs)
            segmentLayout.currentTab = 0
            segmentLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    showHideFragment(fragments[position])
                }

                override fun onTabReselect(position: Int) {

                }

            })
        }
    }


    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.contentView)
                .statusBarColor(R.color.transparent)
                .statusBarColorTransform(R.color.black)
                .statusBarAlpha(0.25f)
                .init()
    }

    override fun bindBackIcon(): Drawable {
        return createDefaultBack()
    }

    override fun bindLayout(): Int = R.layout.fragment_common_tab
}