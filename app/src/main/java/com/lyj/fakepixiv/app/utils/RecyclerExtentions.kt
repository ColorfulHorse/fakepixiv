package com.lyj.fakepixiv.app.utils

import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.databinding.LiveOnPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.widget.StateView
import timber.log.Timber

/**
 * @author greensun
 *
 * @date 2019/4/19
 *
 * @desc
 */

/**
 * 滑动到阈值以后自动加载更多，阈值：[threshold]
 */
inline fun RecyclerView.attachLoadMore(state: ObservableField<LoadState>? = null, threshold: Int = 12, lifecycle: Lifecycle? = null, crossinline loadMore: () -> Unit) {
    state?.doOnPropertyChanged(lifecycle) { _, _ ->
        if (state.get() is LoadState.Failed) {
            Timber.tag("recyclerViewExt").e("加载更多失败....")
            setTag(R.id.tag_recyclerView_loadMore, false)
        }
    }

    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                val adapter = recyclerView.adapter
                adapter?.let {
                    val layoutManager = recyclerView.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        val last = it.itemCount - 1
                        val pos = layoutManager.findLastVisibleItemPosition()
                        if (pos < last) {
                            if (last - pos <= threshold) {
                                var loading = false
                                val tag = recyclerView.getTag(R.id.tag_recyclerView_loadMore)
                                tag?.let {
                                    if (tag is Boolean) {
                                        loading = tag
                                    }
                                }
                                if (!loading) {
                                    Timber.tag("recyclerViewExt").e("加载更多中....")
                                    recyclerView.setTag(R.id.tag_recyclerView_loadMore, true)
                                    loadMore()
                                }
                            } else {
                                recyclerView.setTag(R.id.tag_recyclerView_loadMore, false)
                            }
                        }
                    }
                }
            }
        }
    })
}

fun RecyclerView.attachHeader(view: View) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            var result = -dy
            var target = view.top - dy
            if (dy > 0) {
                if (target == -view.height) {
                    return
                }
                if (target < -view.height) {
                    result = -view.height - view.top
                }
            } else {
                if (target == 0) {
                    return
                }
                if (target > 0) {
                    result = -view.top
                }
            }
            view.layout(view.left, view.top + result, view.right, view.bottom + result)
        }
    })
}

/**
 * 监听列表数据加载状态
 * [lifecycleOwner] 防止匿名内部类造成的内存泄漏
 */
fun BaseQuickAdapter<*, *>.bindState(loadState: ObservableField<LoadState>,
                                     @LayoutRes loadingRes: Int = R.layout.layout_common_loading,
                                     @LayoutRes errorRes: Int = R.layout.layout_error,
                                     onSucceed: (() -> Unit)? = null, onFailed: ((err: Throwable) -> Unit)? = null,
                                     onLoading: (() -> Unit)? = null, refreshLayout: SwipeRefreshLayout? = null,
                                     lifecycle: Lifecycle? = null,
                                     reload: (() -> Unit)? = null) {
    var firstLoad = loadState.get() is LoadState.Idle

    val stateView = StateView(ApplicationLike.context, lifecycle)
    stateView.loadState = loadState
    stateView.reload = {
        reload?.invoke()
    }
    stateView.loadingRes = loadingRes
    stateView.errorRes = errorRes
    emptyView = stateView
    refreshLayout?.setOnRefreshListener {
        reload?.invoke()
    }

    loadState.doOnPropertyChanged(lifecycle) { _, _ ->
        when (val state = loadState.get()) {
            is LoadState.Loading -> {
                if (headerLayoutCount + footerLayoutCount > 0) {
                    notifyDataSetChanged()
                }
                //refreshLayout?.isRefreshing = false
                if (firstLoad) {
                    // 是第一次加载
                    refreshLayout?.isEnabled = false
                }
                onLoading?.invoke()
            }
            is LoadState.Failed -> {
                if (headerLayoutCount + footerLayoutCount > 0) {
                    notifyDataSetChanged()
                }
                refreshLayout?.isRefreshing = false
                refreshLayout?.isEnabled = false
                onFailed?.invoke(state.error)
            }
            is LoadState.Succeed -> {
                firstLoad = false
                refreshLayout?.isRefreshing = false
                refreshLayout?.isEnabled = true
                onSucceed?.invoke()
            }
        }
    }
}

