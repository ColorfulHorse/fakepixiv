package com.lyj.fakepivix.app.utils

import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewParentCompat
import android.support.v4.widget.ViewDragHelper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import com.lyj.fakepivix.R

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
fun RecyclerView.attachLoadMore(threshold: Int = 12, loadMore: () -> Unit) {
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
                                    recyclerView.setTag(R.id.tag_recyclerView_loadMore, true)
                                    loadMore()
                                }
                            }else {
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
            var result = - dy
            var target = view.top - dy
            if (dy > 0) {
                if (target == - view.height) {
                    return
                }
                if (target < - view.height) {
                    result = - view.height - view.top
                }
            }else {
                if (target == 0) {
                    return
                }
                if (target > 0) {
                    result = - view.top
                }
            }
            view.layout(view.left, view.top+result, view.right, view.bottom+result)
        }
    })
}
