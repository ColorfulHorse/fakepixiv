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
class RankIllustViewModel(@IllustCategory val category: String, var mode: String, var date: String = "") : IllustListViewModel() {


    init {
        action = {
            IllustRepository.instance
                    .getRankIllust(mode, category = category, date = date)
                    .map { res ->
                        // 排行榜前三布局不同
                        res.illusts.forEachIndexed { index, illust ->
                            if (index <= 2) {
                                illust.type = Illust.RANK + category
                            }
                        }
                        res
                    }
        }
    }
}