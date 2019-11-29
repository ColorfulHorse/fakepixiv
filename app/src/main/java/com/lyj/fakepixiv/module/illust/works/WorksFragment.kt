package com.lyj.fakepixiv.module.illust.works

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment

import com.lyj.fakepixiv.app.constant.EXTRA_CATEGORY
import com.lyj.fakepixiv.app.constant.EXTRA_ID
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.entity.TabBean
import com.lyj.fakepixiv.databinding.FragmentWorksBinding
import com.lyj.fakepixiv.module.common.IllustListFragment
import com.lyj.fakepixiv.module.common.IllustListViewModel

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc 用户作品列表页
 */
class WorksFragment : BackFragment<FragmentWorksBinding, BaseViewModel?>() {
    override var mViewModel: BaseViewModel? = null

    private val fragments = mutableListOf<FragmentationFragment<*, *>>()
    private var prePosition = 0
    var userId = -1L
    var category = IllustCategory.ILLUST
    var illustCount = 0
    var comicCount = 0
    var novelCount = 0

    companion object {
        const val EXTRA_ILLUST_COUNT = "EXTRA_ILLUST_COUNT"
        const val EXTRA_COMIC_COUNT = "EXTRA_COMIC_COUNT"
        const val EXTRA_NOVEL_COUNT = "EXTRA_NOVEL_COUNT"
        fun newInstance(illustCount: Int = 0,
                        comicCount: Int = 0,
                        novelCount: Int = 0,
                        userId: Long = -1,
                        @IllustCategory category: String = IllustCategory.ILLUST
        ) = WorksFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_ID, userId)
                putString(EXTRA_CATEGORY, category)
                putInt(EXTRA_ILLUST_COUNT, illustCount)
                putInt(EXTRA_COMIC_COUNT, comicCount)
                putInt(EXTRA_NOVEL_COUNT, novelCount)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            userId = it.getLong(EXTRA_ID, -1)
            category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
            illustCount = it.getInt(EXTRA_ILLUST_COUNT, 0)
            comicCount = it.getInt(EXTRA_COMIC_COUNT, 0)
            novelCount = it.getInt(EXTRA_NOVEL_COUNT, 0)
        }
        if (userId == -1L) {
            // 自己
            userId = UserRepository.instance.loginData?.user?.id?:-1L
        }
        mToolbar?.let {
            it.title = getString(R.string.works)
        }
        initFragment()
    }


    private fun initFragment() {
        val tabs = arrayListOf<CustomTabEntity>()
        if (illustCount > 0) {
            tabs.add(TabBean(title = "${getString(R.string.tab_illust)} $illustCount"))
            val illustListFragment = IllustListFragment.newInstance(IllustCategory.ILLUST).apply {
                mViewModel = IllustListViewModel {
                    IllustRepository.instance
                            .loadUserIllust(userId, IllustCategory.ILLUST)
                }
            }
            fragments.add(illustListFragment)
        }
        if (comicCount > 0) {
            tabs.add(TabBean(title = "${getString(R.string.tab_comic)} $comicCount"))
            val comicFragment = IllustListFragment.newInstance(IllustCategory.COMIC).apply {
                mViewModel = IllustListViewModel {
                    IllustRepository.instance
                            .loadUserIllust(userId, IllustCategory.COMIC)
                }
            }
            fragments.add(comicFragment)
        }

        if (novelCount > 0) {
            tabs.add(TabBean(title = "${getString(R.string.tab_novel)} $novelCount"))
            val novelFragment = IllustListFragment.newInstance(IllustCategory.NOVEL).apply {
                mViewModel = IllustListViewModel {
                    IllustRepository.instance
                            .loadUserNovels(userId)
                }
            }
            fragments.add(novelFragment)
        }

        val position = when (category) {
            IllustCategory.ILLUST -> 0
            IllustCategory.COMIC -> if (fragments.size > 1) 1 else 0
            IllustCategory.NOVEL -> if (fragments.size > 1) fragments.size - 1 else  0
            else -> 0
        }
        prePosition = position

        loadMultipleRootFragment(R.id.fl_container, position, *fragments.toTypedArray())
        with(mBinding) {
            tabLayout.setTabData(tabs)
            tabLayout.currentTab = position
            tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    switchTab(position)
                }

                override fun onTabReselect(position: Int) {

                }
            })
        }
    }

    /**
     * 切换tab页
     */
    private fun switchTab(position: Int) {
        showHideFragment(fragments[position], fragments[prePosition])
        prePosition = position
    }


    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.contentView)
                .statusBarColor(R.color.transparent)
                .statusBarColorTransform(R.color.black)
                .statusBarAlpha(0.25f)
                .init()
    }

    override fun bindBackIcon(): Drawable = createDefaultBack()

    override fun bindLayout(): Int = R.layout.fragment_works
}