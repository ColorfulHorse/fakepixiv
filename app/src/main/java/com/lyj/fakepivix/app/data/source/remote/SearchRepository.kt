package com.lyj.fakepivix.app.data.source.remote

import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.LiveListResp
import com.lyj.fakepivix.app.data.model.response.SearchTagListResp
import com.lyj.fakepivix.app.data.model.response.TrendTag
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/5/29
 *
 * @desc
 */
class SearchRepository {
    companion object {
        val instance by lazy { SearchRepository() }
    }


    fun loadIllustSearchTag(@IllustCategory category: String): Observable<SearchTagListResp> {
        return RetrofitManager.instance
                .apiService
                .getIllustSearchTag(category)
                .map {
                    it.trend_tags[0].type = TrendTag.TYPE_HEADER
                    it
                }
                .schedulerTransform()
    }
}