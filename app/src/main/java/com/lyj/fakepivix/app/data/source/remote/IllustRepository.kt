package com.lyj.fakepivix.app.data.source.remote

import android.util.SparseArray
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class IllustRepository private constructor() {

    var recommendNext = ""

    private val illustList: SparseArray<Illust> = SparseArray()

    companion object {
        val instance by lazy { IllustRepository() }
    }

    fun loadRecommend(): Observable<IllustListResp> {
        return RetrofitManager.instance
                .apiService
                .getIllustRecommendData()
                .doOnNext {
                    with(it) {
                        recommendNext = next_url
                        illusts.forEach {
                            illust -> illustList.put(illust.id, illust)
                        }
                        ranking_illusts.forEach {
                            illust -> illustList.put(illust.id, illust)
                        }
                    }
                }
    }

}