package com.lyj.fakepivix.module.illust.bookmark

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.CommonFragmentAdapter
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.app.data.source.remote.UserRepository
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.databinding.FragmentBookmarkBinding
import com.lyj.fakepivix.databinding.FragmentFollowingBinding
import com.lyj.fakepivix.module.common.IllustListFragment
import com.lyj.fakepivix.module.common.IllustListViewModel
import com.lyj.fakepivix.module.common.UserListFragment
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import kotlinx.coroutines.rx2.rxObservable

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class BookmarkFragment : BackFragment<FragmentBookmarkBinding, BaseViewModel<IModel?>?>() {
    override var mViewModel: BaseViewModel<IModel?>? = null

    private val fragments = mutableListOf<FragmentationFragment<*,*>>()
    var prePosition = 0
    var userId = ""

    companion object {
        const val TAG_PUBLIC = "TAG_PUBLIC"
        const val TAG_PRIVATE = "TAG_PRIVATE"

        fun newInstance() = BookmarkFragment()
    }

    override fun init(savedInstanceState: Bundle?) {
        userId = UserRepository.instance.loginData?.user?.id.toString()
        mToolbar?.title = getString(R.string.bookmark)
        initFragment()
    }



    private fun initFragment() {
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_pic)),
                TabBean(title = getString(R.string.tab_novel))
        )
        val illustListFragment = IllustListFragment.newInstance(IllustCategory.ILLUST).apply {
            mViewModel = IllustListViewModel()
        }
        //val adapter = CommonFragmentAdapter(childFragmentManager, )
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