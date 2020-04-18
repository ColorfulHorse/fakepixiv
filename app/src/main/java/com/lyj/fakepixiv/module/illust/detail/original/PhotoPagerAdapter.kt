package com.lyj.fakepixiv.module.illust.detail.original

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.module.illust.detail.IllustDetailFragment

/**
 * @author green sun
 *
 * @date 2019/11/11
 *
 * @desc
 */
class PhotoPagerAdapter(val data: List<Illust>, val target: Int, val callback : (PhotoPage) -> Unit) : PagerAdapter() {
    val pages: SparseArray<PhotoPage> = SparseArray()

    override fun isViewFromObject(view: View, page: Any): Boolean = view == (page as PhotoPage).view

    override fun getCount(): Int = data.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val page = PhotoPage(container.context, data[position])
        pages.put(position, page)
        container.addView(page.view)
        if (position == target) {
            callback(page)
        }
        return page
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        pages.remove(position)
        val page = `object` as PhotoPage
        page.destroy()
        container.removeView(page.view)
    }
}