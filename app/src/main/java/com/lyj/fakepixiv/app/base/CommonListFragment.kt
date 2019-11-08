package com.lyj.fakepixiv.app.base

import androidx.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.adapter.BaseMultiBindingAdapter
import com.lyj.fakepixiv.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepixiv.databinding.FragmentCommonListBinding
import com.lyj.fakepixiv.module.common.adapter.IllustAdapter
import com.lyj.fakepixiv.module.novel.series.NovelSeriesFragment

/**
 * @author greensun
 *
 * @date 2019/3/24
 *
 * @desc
 */
abstract class CommonListFragment<V : ViewDataBinding, VM: BaseViewModel?> : BackFragment<V, VM>() {

    override fun initImmersionBar() {
        val contentView = mBinding.root.findViewById<View>(R.id.contentView)
        contentView?.let {
            ImmersionBar.with(this)
                    .titleBarMarginTop(it)
                    .statusBarColor(R.color.transparent)
                    .statusBarColorTransform(R.color.black)
                    .statusBarAlpha(0.25f)
                    .init()
        }
    }

    override fun bindLayout(): Int = R.layout.fragment_common_list
}