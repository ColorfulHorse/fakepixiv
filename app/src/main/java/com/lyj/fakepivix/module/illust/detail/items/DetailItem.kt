package com.lyj.fakepivix.module.illust.detail.items

/**
 * @author greensun
 *
 * @date 2019/9/24
 *
 * @desc
 */
interface DetailItem {

    var type: Int

    companion object {
        const val LAYOUT_DESC = 111
        const val LAYOUT_SERIES = 222
        const val LAYOUT_USER = 333
        const val LAYOUT_COMMENT = 444
        const val LAYOUT_RELATED_CAPTION = 555
    }

}