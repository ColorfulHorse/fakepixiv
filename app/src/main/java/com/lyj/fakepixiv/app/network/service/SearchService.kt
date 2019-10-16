package com.lyj.fakepixiv.app.network.service

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.LoginResp
import com.lyj.fakepixiv.app.data.model.response.SearchTagListResp
import com.lyj.fakepixiv.app.data.model.response.TagListResp
import com.lyj.fakepixiv.app.network.retrofit.interceptors.SwitchBaseUrlInterceptor
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author green sun
 *
 * @date 2019/10/16
 *
 * @desc
 */
interface SearchService {
    /**
     * 获取热门搜索标签
     * [category] 插画漫画/小说
     */
    @GET("/v1/trending-tags/{category}")
    fun getIllustSearchTag(@IllustCategory @Path("category")category: String): Observable<SearchTagListResp>

    /**
     * 自动完成搜索关键字
     */
    @GET("/v2/search/autocomplete")
    fun searchAutoComplete(@Query("word")keyword: String): Observable<TagListResp>
}