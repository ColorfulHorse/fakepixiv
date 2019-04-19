package com.lyj.fakepivix.module.login

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.animation.LinearInterpolator
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.base.FragmentationActivity
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.ActivityLoginBinding
import com.lyj.fakepivix.databinding.ItemWallpaperBinding
import com.lyj.fakepivix.module.login.login.LoginFragment
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/3/16
 *
 * @desc 登录页图片墙
 */
class LoginActivity : FragmentationActivity<ActivityLoginBinding, WallpaperViewModel>() {

    override var mViewModel: WallpaperViewModel = WallpaperViewModel()
    lateinit var mAdapter: WallpaperAdapter
    //private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        ImmersionBar.with(this).init()
        val fragment = findFragment(LoginFragment::class.java)
        if (fragment == null) {
            loadRootFragment(R.id.fragmentContainer, LoginFragment.newInstance())
        }
        with(mViewModel) {
            with(mBinding) {
                // 自动滚动
                mAdapter = WallpaperAdapter(data) {
                    overlayVisibility.set(false)
                    recyclerView.smoothScrollToPosition(recyclerView.adapter?.itemCount!! - 4)
                    //recyclerView.smoothScrollBy(0, 20, LinearInterpolator())
                }
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            val adapter = recyclerView.adapter
                            adapter?.let {
                                val layoutManager = recyclerView.layoutManager
                                if (layoutManager is LinearLayoutManager) {
                                    val pos = layoutManager.findLastVisibleItemPosition()
                                    if (pos < adapter.itemCount - 1) {
                                        recyclerView.smoothScrollBy(0, 20, LinearInterpolator())
                                    }else {
                                        val last = recyclerView.getChildAt(recyclerView.childCount - 1)
                                        val distance = last.bottom - recyclerView.bottom
                                        if (distance > 0) {
                                            recyclerView.smoothScrollBy(0, 20, LinearInterpolator())
                                        }
                                    }
                                }
                            }
                        }
                    }
                })

                val layoutManager = GridLayoutManager(this@LoginActivity, 2, LinearLayoutManager.VERTICAL, false)
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(
                        CommonItemDecoration.Builder()
                                .draw(false)
                                .verticalWidth(3.5f.dp2px())
                                .build())
                mAdapter.bindToRecyclerView(recyclerView)
                val sizeProvider = ViewPreloadSizeProvider<Illust>()
                mAdapter.sizeProvider = sizeProvider
                val recyPreloader = RecyclerViewPreloader<Illust>(this@LoginActivity, mAdapter, sizeProvider, 10)
                recyclerView.addOnScrollListener(recyPreloader)
            }
        }
    }

    override fun bindLayout(): Int = R.layout.activity_login


    override fun onDestroy() {
        //disposable?.dispose()
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }
}
