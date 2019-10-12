package com.lyj.fakepivix.module.illust.works

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.EXTRA_CATEGORY
import com.lyj.fakepivix.app.constant.EXTRA_ID
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.FragmentWorksBinding
import com.lyj.fakepivix.module.common.IllustListFragment
import com.lyj.fakepivix.module.common.IllustListViewModel
import me.yokeyword.fragmentation.ISupportFragment

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc 用户作品列表页
 */
class WorksFragment : BackFragment<FragmentWorksBinding, BaseViewModel<IModel?>?>() {
    override var mViewModel: BaseViewModel<IModel?>? = null

    private val fragments = mutableListOf<FragmentationFragment<*, *>>()
    private var prePosition = 0
    var userId = ""
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
                        userId: String = "",
                        @IllustCategory category: String = IllustCategory.ILLUST
        ) = WorksFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_ID, userId)
                putString(EXTRA_CATEGORY, category)
                putInt(EXTRA_ILLUST_COUNT, illustCount)
                putInt(EXTRA_COMIC_COUNT, comicCount)
                putInt(EXTRA_NOVEL_COUNT, novelCount)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            userId = it.getString(EXTRA_ID, "")
            category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
            illustCount = it.getInt(EXTRA_ILLUST_COUNT, 0)
            comicCount = it.getInt(EXTRA_COMIC_COUNT, 0)
            novelCount = it.getInt(EXTRA_NOVEL_COUNT, 0)
        }
        if (userId.isEmpty()) {
            // 自己
            userId = UserRepository.instance.loginData?.user?.id.toString()
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