package com.lyj.fakepivix.module.illust.ranking

import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.source.remote.IllustRepository
import com.lyj.fakepivix.module.common.IllustListViewModel
import java.util.*

/**
 * @author greensun
 *
 * @date 2019/9/10
 *
 * @desc
 */
class RankIllustViewModel(@IllustCategory val category: String, var mode: String, var date: String = "") : IllustListViewModel({
    IllustRepository.instance
            .getRankIllust(mode, category = category, date = date)
            .map {res ->
                // 排行榜前三布局不同
                res.illusts.forEachIndexed { index, illust ->
                    if (index <= 2) {
                        illust.type = Illust.RANK + category
                    }
                }
                res
            }
}) {
    init {
        val c = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)
        date = String.format("%d-%02d-%02d", y, m+1, d)
    }

}