package com.lyj.fakepivix.app.utils

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
            var result = view.translationY - dy
            if (dy > 0) {
//                if (translationY == 0f)
//                    return
                if (result < - view.height) {
                    result = - view.height.toFloat()
                }
            }else {
//                if (translationY == - view.height.toFloat())
//                    return
                if (result > 0) {
                    result = 0f
                }
            }
            view.translationY = result
        }
    })
}
