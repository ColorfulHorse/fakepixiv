package com.lyj.fakepixiv.module.user.following

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.base.IModel
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.source.remote.UserRepository
import com.lyj.fakepixiv.app.entity.TabBean
import com.lyj.fakepixiv.databinding.FragmentFollowingBinding
import com.lyj.fakepixiv.module.common.UserListFragment
import com.lyj.fakepixiv.module.common.UserListViewModel

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class FollowingFragment : BackFragment<FragmentFollowingBinding, BaseViewModel<IModel?>?>() {
    override var mViewModel: BaseViewModel<IModel?>? = null

    private val fragments = mutableListOf<FragmentationFragment<*,*>>()
    var prePosition = 0
    var userId = ""

    companion object {
        const val TAG_PUBLIC = "TAG_PUBLIC"
        const val TAG_PRIVATE = "TAG_PRIVATE"

        fun newInstance() = FollowingFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        userId = UserRepository.instance.loginData?.user?.id.toString()
        mToolbar?.title = getString(R.string.follow)
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_public)),
                TabBean(title = getString(R.string.tab_private))
        )
        with(mBinding) {
            segmentLayout.setTabData(tabs)
            segmentLayout.currentTab = 0
            segmentLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    switchTab(position)
                }

                override fun onTabReselect(position: Int) {

                }
            })
        }
        initFragment()
    }



    private fun initFragment() {
        var publicFragment = findChildFragment<UserListFragment>(TAG_PUBLIC)
        var privateFragment = findChildFragment<UserListFragment>(TAG_PRIVATE)
        if (publicFragment == null) {
            publicFragment = UserListFragment.newInstance()
            publicFragment.mViewModel = UserListViewModel {
                UserRepository.instance
                        .getFollowing(userId, Restrict.PUBLIC)
            }
            privateFragment = UserListFragment.newInstance()
            privateFragment.mViewModel = UserListViewModel {
                UserRepository.instance
                        .getFollowing(userId, Restrict.PRIVATE)
            }
            loadMultipleRootFragment(R.id.fl_container, 0, publicFragment, privateFragment)
//            extraTransaction()
//                    .setTag(TAG_PUBLIC)
//                    .loadRootFragment(R.id.fl_container, publicFragment)
//
//            extraTransaction().setTag(TAG_PRIVATE)
//                    .loadRootFragment(R.id.fl_container, privateFragment)
        }

        fragments.clear()
        fragments.add(publicFragment)
        privateFragment?.let { fragments.add(it) }
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

    override fun bindBackIcon(): Drawable {
        return DrawerArrowDrawable(mActivity).apply {
            progress = 1F
            color = Color.WHITE
        }
    }

    override fun bindLayout(): Int = R.layout.fragment_following
}