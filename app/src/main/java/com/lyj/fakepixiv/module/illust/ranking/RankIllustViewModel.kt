package com.lyj.fakepixiv.module.illust.ranking

import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.module.common.IllustListViewModel

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
            var res = IllustRepository.instance
                    .getRankIllust(mode, category = category, date = date)
            res.illusts.forEachIndexed { index, illust ->
                if (index <= 2) {
                    illust.type = Illust.RANK + category
                }
            }
            res
//                    .map { res ->
//                        // 排行榜前三布局不同
//                        res.illusts.forEachIndexed { index, illust ->
//                            if (index <= 2) {
//                                illust.type = Illust.RANK + category
//                            }
//                        }
//                        res
//                    }
        }
    }
}