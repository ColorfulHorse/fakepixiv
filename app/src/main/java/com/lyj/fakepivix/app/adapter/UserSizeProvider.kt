package com.lyj.fakepivix.app.adapter

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.transition.Transition

/**
 * @author greensun
 *
 * @date 2019/8/6
 *
 * @desc 用户列表预加载
 */
class UserSizeProvider<T> : ListPreloader.PreloadSizeProvider<T> {

    private var avatarSize: IntArray? = null
    private var illustSize: IntArray? = null
    private var avatarViewTarget: SizeViewTarget? = null
    private var illustViewTarget: SizeViewTarget? = null



    override fun getPreloadSize(item: T, adapterPosition: Int, itemPosition: Int): IntArray? {
        if (itemPosition == 0) {
            avatarSize?.let {
                return it.copyOf(it.size)
            }
        }else {
            illustSize?.let {
                return it.copyOf(it.size)
            }
        }
        return null
    }


    fun setAvatarView(view: View) {
        if (avatarSize != null || avatarViewTarget != null) {
            return
        }
        avatarViewTarget = SizeViewTarget(view, SizeReadyCallback { width, height ->
            avatarSize = intArrayOf(width, height)
            avatarViewTarget = null
        })
    }

    fun setIllistView(view: View) {
        if (illustSize != null || illustViewTarget != null) {
            return
        }
        illustViewTarget = SizeViewTarget(view, SizeReadyCallback { width, height ->
            illustSize = intArrayOf(width, height)
            illustViewTarget = null
        })
    }

    private class SizeViewTarget internal constructor(view: View, callback: SizeReadyCallback) : CustomViewTarget<View, Any>(view) {
        init {
            getSize(callback)
        }

        override fun onResourceReady(resource: Any, transition: Transition<in Any>?) {
            // Do nothing
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {

        }

        override fun onResourceCleared(placeholder: Drawable?) {

        }
    }
}