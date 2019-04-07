package com.lyj.fakepivix.module.login

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.animation.LinearInterpolator
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.GlideApp
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
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
    lateinit var adapter: WallpaperAdapter
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
        // 自动滚动
        adapter = WallpaperAdapter(mViewModel.data) {
            mViewModel.overlayVisibility.set(false)
            mBinding.recyclerView.smoothScrollBy(0, 20, LinearInterpolator())
//            disposable = Observable.interval(250, TimeUnit.MILLISECONDS)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeBy {
//                        mBinding.recyclerView.smoothScrollBy(0, 20, LinearInterpolator())
//                    }
        }
        mBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val view = recyclerView.getChildAt(recyclerView.childCount-1)
                    if (view.bottom != recyclerView.bottom) {
                        recyclerView.smoothScrollBy(0, 20, LinearInterpolator())
                    }
                }
            }
        })
        val layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        mBinding.recyclerView.layoutManager = layoutManager
        mBinding.recyclerView.addItemDecoration(
                CommonItemDecoration.Builder()
                        .draw(false)
                        .verticalWidth(dp2px(3.5f))
                        .build())
        adapter.bindToRecyclerView(mBinding.recyclerView)

        mBinding.recyclerView.setItemViewCacheSize(0)
        // 回收时取消
        mBinding.recyclerView.setRecyclerListener {
            val holder = it as BaseBindingAdapter.BaseBindingViewHolder<ItemWallpaperBinding>
            if (holder.binding != null) {
                GlideApp.with(this).clear(holder.binding.img)
            }
        }

        val sizeProvider = ViewPreloadSizeProvider<Illust>()
        val recyPreloader = RecyclerViewPreloader<Illust>(this, adapter, sizeProvider, 10)
        mBinding.recyclerView.addOnScrollListener(recyPreloader)
    }

    override fun bindLayout(): Int = R.layout.activity_login


    override fun onDestroy() {
        //disposable?.dispose()
        super.onDestroy()
        ImmersionBar.with(this).destroy()
    }
}
