package com.lyj.fakepivix.module.login

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationActivity
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.ActivityLoginBinding
import com.lyj.fakepivix.module.login.login.LoginFragment
import com.lyj.fakepivix.widget.CommonItemDecoration
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit

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
    private var disposable: Disposable? = null

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .init()
        if (findFragment(LoginFragment::class.java) == null) {
            loadRootFragment(R.id.fragmentContainer, LoginFragment.newInstance())
        }
        // 自动滚动
        adapter = WallpaperAdapter(mViewModel.data) {
            mViewModel.overlayVisibility.set(false)
            disposable = Observable.interval(100, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy {
                        mBinding.recyclerView.smoothScrollBy(0, 5)
                    }
        }
        val layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        mBinding.recyclerView.layoutManager = layoutManager
        mBinding.recyclerView.addItemDecoration(
                CommonItemDecoration.Builder()
                .draw(false)
                .verticalWidth(dp2px(3.5f))
                .build())
        adapter.bindToRecyclerView(mBinding.recyclerView)


//        val sizeProvider = ViewPreloadSizeProvider<Illust>()
//
//        val recyPreloader = RecyclerViewPreloader<Illust>(this, modelProvider, sizeProvider, 10)
//        mBinding.recyclerView.addOnScrollListener(recyPreloader)
    }

    override fun bindLayout(): Int = R.layout.activity_login


    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}
