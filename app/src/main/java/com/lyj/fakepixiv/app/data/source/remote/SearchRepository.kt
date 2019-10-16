package com.lyj.fakepixiv.app.data.source.remote

import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.SearchTagListResp
import com.lyj.fakepixiv.app.data.model.response.Tag
import com.lyj.fakepixiv.app.data.model.response.TrendTag
import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.service.PixivisionService
import com.lyj.fakepixiv.app.network.service.SearchService
import com.lyj.fakepixiv.app.reactivex.schedulerTransform
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/5/29
 *
 * @desc
 */
class SearchRepository {

    private val service: SearchService by lazy { RetrofitManager.instance.searchService }

    companion object {
        val instance by lazy { SearchRepository() }
    }


    fun loadIllustSearchTag(@IllustCategory category: String): Observable<SearchTagListResp> {
        return service
                .getIllustSearchTag(category)
                .map {
                    it.trend_tags[0].type = TrendTag.TYPE_HEADER
                    it.trend_tags.forEach { tag -> tag.illust.type = category }
                    it
                }
                .schedulerTransform()
    }

    fun searchAutoComplete(keyword: String): Observable<List<Tag>> {
        return service
                .searchAutoComplete(keyword)
                .map {
                    it.tags
                }
                .schedulerTransform()
    }
}