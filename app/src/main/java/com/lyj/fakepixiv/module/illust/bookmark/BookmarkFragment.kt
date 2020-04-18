package com.lyj.fakepixiv.module.illust.bookmark

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.graphics.drawable.DrawableCompat
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.CommonFragmentAdapter
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment

import com.lyj.fakepixiv.app.constant.EXTRA_CATEGORY
import com.lyj.fakepixiv.app.constant.EXTRA_ID
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.databinding.FragmentBookmarkBinding
import com.lyj.fakepixiv.module.common.IllustListFragment
import com.lyj.fakepixiv.module.common.IllustListViewModel

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc 用户收藏页
 */
class BookmarkFragment : BackFragment<FragmentBookmarkBinding, BaseViewModel?>() {
    override var mViewModel: BaseViewModel? = null

    private val fragments = mutableListOf<FragmentationFragment<*,*>>()
    var userId = -1L
    var category = IllustCategory.ILLUST
    // 选中标签
    var publicTag = ""
    var privateTag = ""
    // 收藏标签
    var filterTag = publicTag
    var restrict = Restrict.PUBLIC
    private lateinit var illustVm: IllustListViewModel
    private lateinit var novelVm: IllustListViewModel

//    val filterDialog by lazy { FilterDialog.newInstance(category, publicTag, privateTag) { restrict, tag ->
//        filterTag = tag
//        this.restrict = restrict
//        if (restrict == Restrict.PUBLIC) {
//            publicTag = tag
//        }else {
//            privateTag = tag
//        }
//        if (mBinding.viewPager.currentItem == 0) {
//            illustVm.load()
//        }else {
//            novelVm.load()
//        }
//    } }

    companion object {

        fun newInstance(userId: Long = -1, @IllustCategory category: String = IllustCategory.ILLUST) = BookmarkFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_ID, userId)
                putString(EXTRA_CATEGORY, category)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            userId = it.getLong(EXTRA_ID, -1)
            category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
        }
        if (userId == -1L) {
            // 自己
            userId = UserRepository.instance.loginData?.user?.id?:-1L
        }
        mToolbar?.let {
            it.title = getString(R.string.bookmark)
            if (userId == UserRepository.instance.loginData?.user?.id) {
                it.inflateMenu(R.menu.menu_bookmark)
                val icon = ContextCompat.getDrawable(mActivity, R.drawable.ic_search_filter)
                icon?.let { drawable ->
                    DrawableCompat.setTint(drawable, Color.WHITE)
                    it.menu.findItem(R.id.restrict).setIcon(drawable)
                }
                it.setOnMenuItemClickListener { menu ->
                    val filterDialog = FilterDialog.newInstance(category, restrict, publicTag, privateTag) { restrict, tag ->
                        filterTag = tag
                        this.restrict = restrict
                        if (restrict == Restrict.PUBLIC) {
                            publicTag = tag
                        }else {
                            privateTag = tag
                        }
                        if (mBinding.viewPager.currentItem == 0) {
                            illustVm.load()
                        }else {
                            novelVm.load()
                        }
                    }
                    filterDialog.show(childFragmentManager, "FilterDialog")
                    true
                }
            }
        }
        initFragment()
    }



    private fun initFragment() {
        val illustListFragment = IllustListFragment.newInstance(IllustCategory.ILLUST).apply {
            illustVm = IllustListViewModel {
                IllustRepository.instance
                        .loadUserBookmarks(userId, config.category,restrict, filterTag)
            }
            mViewModel = illustVm
        }
        val novelFragment = IllustListFragment.newInstance(IllustCategory.NOVEL).apply {
            novelVm = IllustListViewModel {
                IllustRepository.instance
                        .loadUserBookmarks(userId, category, restrict, filterTag)
            }
            mViewModel = novelVm
        }
        fragments.add(illustListFragment)
        fragments.add(novelFragment)
        val adapter = CommonFragmentAdapter(childFragmentManager, fragments, arrayOf(getString(R.string.tab_pic), getString(R.string.tab_novel)))
        with(mBinding) {
            viewPager.adapter = adapter
            segmentLayout.setViewPager(viewPager)
            segmentLayout.currentTab = if (category == IllustCategory.ILLUST) 0 else 1
            segmentLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    category = if (position == 0) IllustCategory.ILLUST else IllustCategory.NOVEL
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

    override fun bindLayout(): Int = R.layout.fragment_bookmark
}