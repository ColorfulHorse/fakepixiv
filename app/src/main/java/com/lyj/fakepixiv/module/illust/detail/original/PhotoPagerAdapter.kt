package com.lyj.fakepixiv.module.illust.detail.original

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.lyj.fakepixiv.app.data.model.response.Illust

/**
 * @author green sun
 *
 * @date 2019/11/11
 *
 * @desc
 */
class PhotoPagerAdapter(val data: List<Illust>, val target: Int, val callback : (PhotoPage) -> Unit) : PagerAdapter() {
    override fun isViewFromObject(view: View, page: Any): Boolean = view == (page as PhotoPage).view

    override fun getCount(): Int = data.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val page = PhotoPage(container.context, data[position])
        container.addView(page.view)
        if (position == target) {
            callback(page)
        }
        return page
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val page = `object` as PhotoPage
        page.destroy()
        container.removeView(page.view)
    }
}