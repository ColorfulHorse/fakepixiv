package com.lyj.fakepivix.module.illust.bookmark

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.CommonFragmentAdapter
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.databinding.FragmentBookmarkBinding
import com.lyj.fakepivix.module.common.IllustListFragment
import com.lyj.fakepivix.module.common.IllustListViewModel

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc 用户收藏页
 */
class BookmarkFragment : BackFragment<FragmentBookmarkBinding, BaseViewModel<IModel?>?>() {
    override var mViewModel: BaseViewModel<IModel?>? = null

    private val fragments = mutableListOf<FragmentationFragment<*,*>>()
    var userId = ""
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
        const val TAG_PUBLIC = "TAG_PUBLIC"
        const val TAG_PRIVATE = "TAG_PRIVATE"

        fun newInstance() = BookmarkFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        userId = UserRepository.instance.loginData?.user?.id.toString()
        mToolbar?.let {
            it.title = getString(R.string.bookmark)
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
        initFragment()
    }



    private fun initFragment() {

        val illustListFragment = IllustListFragment.newInstance(IllustCategory.ILLUST).apply {
            illustVm = IllustListViewModel {
                IllustRepository.instance
                        .loadUserBookmarks(userId, category,restrict, filterTag)
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
            segmentLayout.currentTab = 0
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
        return DrawerArrowDrawable(mActivity).apply {
            progress = 1F
            color = Color.WHITE
        }
    }

    override fun bindLayout(): Int = R.layout.fragment_bookmark
}