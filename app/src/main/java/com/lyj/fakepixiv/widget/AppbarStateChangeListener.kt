package com.lyj.fakepixiv.widget

import com.google.android.material.appbar.AppBarLayout

/**
 * @author greensun
 *
 * @date 2019/8/27
 *
 * @desc
 */
class AppbarStateChangeListener(val onStateChange: (appBarLayout: AppBarLayout, state: Int) -> Unit) : AppBarLayout.OnOffsetChangedListener {
    private var state = STATE_EXPANDED
    companion object {
        const val STATE_EXPANDED = 0
        const val STATE_COLLAPSED = 1
        //const val STATE_HIDE = 2
    }

    override fun onOffsetChanged(appbar: AppBarLayout, offset: Int) {
        when {
            offset == 0 -> {
                if (state != STATE_EXPANDED) {
                    state = STATE_EXPANDED
                    onStateChange(appbar, state)
                }
            }

            Math.abs(offset) >= appbar.totalScrollRange -> {
                if (state != STATE_COLLAPSED) {
                    state = STATE_COLLAPSED
                    onStateChange(appbar, state)
                }
            }
        }
    }
}