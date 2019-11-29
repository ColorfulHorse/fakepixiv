package com.lyj.fakepixiv.app.data.source.remote

import androidx.databinding.ObservableField
import android.util.SparseArray
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.IllustCategory.*
import com.lyj.fakepixiv.app.constant.Restrict
import com.lyj.fakepixiv.app.data.model.response.*
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.network.retrofit.RetrofitManager
import com.lyj.fakepixiv.app.network.service.IllustService
import com.lyj.fakepixiv.app.reactivex.schedulerTransform
import com.lyj.fakepixiv.app.utils.JsonUtil
import com.lyj.fakepixiv.app.utils.ioTask
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.awaitSingle
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.http.Query
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc 插画、漫画、小说
 */
class IllustRepository private constructor() {
    
    val service: IllustService by lazy { RetrofitManager.instance.illustService }
    
    companion object {
        val instance by lazy { IllustRepository() }
    }

    val illusts = SparseArray<List<Illust>>()

    operator fun get(key: Int): List<Illust> {
        return illusts[key]
    }

    operator fun set(key: Int, value: List<Illust>) {
        illusts.put(key, value)
    }

    operator fun minus(key: Int) {
        illusts.remove(key)
    }

    /**
     * 获取推荐
     */
    fun getWallPaperData(): Observable<IllustListResp> {
        return service.getWallPaperData()
                .schedulerTransform()
    }

    /**
     * 获取推荐
     */
    fun loadRecommendIllust(@IllustCategory category: String): Observable<IllustListResp> {
        return service.getRecommendIllust(category)
                .schedulerTransform()
    }

    /**
     * 获取排行榜
     * [category] illust插画、漫画 novel小说
     */
    suspend fun getRankIllust(mode: String, date: String = "", @IllustCategory category: String = ILLUST): IllustListResp {
        val realCategory = if (category == NOVEL) NOVEL else ILLUST
        return service.getRankIllust(realCategory, mode, date)
    }


    /**
     * 获取关注的
     * [filter] 筛选条件
     */
    suspend fun loadFollowedIllust(@IllustCategory category: String, @Restrict filter: String = Restrict.ALL): IllustListResp {
        
        return when(category) {
            ILLUST, COMIC -> service.getFollowIllustData(restrict = filter)
            else -> service.getFollowNovelData(restrict = filter)
        }
    }

    /**
     * 获取最新的
     */
    suspend fun loadNewIllust(@IllustCategory category: String): IllustListResp {
        
        return when(category) {
            ILLUST, COMIC -> service.getNewIllustData(category = category)
            else -> service.getNewNovelData()
        }
    }

    /**
     * 获取好P友
     */
    suspend fun loadFriendIllust(@IllustCategory category: String): IllustListResp {
        
        return when(category) {
            OTHER -> service.getFriendIllustData()
            else -> service.getFriendNovelData()
        }
    }

    /**
     * 获取小说详情
     */
    suspend fun getNovelText(novelId: String): NovelText {
        return service.getNovelText(novelId)
    }

    /**
     * 获取用户作品
     */
    suspend fun loadUserIllust(userId: Long, @IllustCategory category: String = ILLUST): IllustListResp {
        return service.getUserIllustData(userId, category)
    }


    /**
     * 获取用户作品
     */
    suspend fun loadUserNovels(userId: Long): IllustListResp {
        return service.getUserNovels(userId)
    }

    /**
     * 获取用户收藏
     */
    suspend fun loadUserBookmarks(@Query("user_id")userId: Long, @IllustCategory category: String = ILLUST,
                                  @Restrict restrict: String = Restrict.PUBLIC, @Query("tag")tag: String = ""): IllustListResp {
        return service.getUserBookmarks(category, userId, restrict, tag)
    }

    /**
     * 获取illust收藏标签列表
     */
    suspend fun getBookMarkDetail(id: String, @IllustCategory category: String): BookMarkResp {
        return if (category == NOVEL) {
            service.getNovelBookmark(id)
        }else {
            service.getIllustBookmark(id)
        }
    }

    /**
     * 获取所有收藏标签列表
     */
    suspend fun getBookMarkTags(@IllustCategory category: String, @Restrict restrict: String = Restrict.PUBLIC): BookmarkTags {
        UserRepository.instance.loginData?.user?.id?.let {
            return service.getBookmarkTag(category, it, restrict)
        }
        return BookmarkTags()
    }


    /**
     * 获取相关作品
     */
    fun loadRelatedIllust(illustId: String): Observable<IllustListResp> {
        return service
                .getRelatedIllustData(illustId)
                .checkEmpty()
                .schedulerTransform()
    }

    suspend fun getSeriesContext(illustId: String): SeriesContextResp {
        return service
                .getSeriesContext(illustId)
    }

    suspend fun getSeriesDetail(seriesId: String): SeriesExt {
        return service
                .getMangaSeries(seriesId)
    }

    suspend fun getNovelSeriesDetail(seriesId: String): NovelSeries {
        return service
                .getNovelSeries(seriesId)
    }

    suspend fun getIllustDetail(illustId: String): IllustResp =
            RetrofitManager
            .instance.illustService
            .getIllustDetail(illustId)


    /**
     * 按顺序搜索
     */
    suspend fun searchIllust(@IllustCategory category: String,
                     keyword: String,
                     asc: Boolean,
                     start: String = "",
                     end: String = "" ,
                     strategy: String = Constant.Request.KEY_SEARCH_PARTIAL
    ): IllustListResp {
        return service
                .searchIllust(category, keyword, if (asc) "date_asc" else "date_desc", strategy, start, end)
    }

    /**
     * 按热门搜索
     */
    suspend fun searchPopularIllust(@IllustCategory category: String,
                     keyword: String,
                     start: String = "",
                     end: String = "" ,
                     strategy: String = Constant.Request.KEY_SEARCH_PARTIAL
    ): IllustListResp {
        return service
                .searchPopularIllust(category, keyword, strategy, start, end)
    }

    /**
     * 获取作品评论
     */
    fun loadIllustComment(illustId: String): Observable<CommentListResp> {
        return service
                .getIllustComment(illustId)
                .schedulerTransform()
    }

    fun loadMoreComment(nextUrl: String): Observable<CommentListResp> {
        return service
                .getIllustComment(nextUrl)
                .schedulerTransform()
    }

    /**
     * 收藏/取消收藏
     */
    private fun starIllust(illustId: String, star: Boolean, tags: List<String> = listOf(), @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        val tagMap = IdentityHashMap<String, String>()
        tags.forEach {
            tagMap[StringBuilder("tags[]").toString()] = it
        }
        return if (star)
            service
                .starIllust(illustId, tagMap, restrict)
                .schedulerTransform()
        else
            service
                    .unStarIllust(illustId)
                    .schedulerTransform()

    }

    /**
     * 收藏小说/取消收藏
     */
    private fun starNovel(novelId: String, star: Boolean, tags: List<String> = listOf(), @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        val tagMap = IdentityHashMap<String, String>()
        tags.forEach {
            tagMap[StringBuilder("tags[]").toString()] = it
        }
        return if (star)
            service
                    .starNovel(novelId, tagMap, restrict)
                    .schedulerTransform()
        else
            service
                    .unStarNovel(novelId)
                    .schedulerTransform()
    }

    /**
     * 收藏/取消收藏
     */
    fun star(illust: Illust, loadState: ObservableField<LoadState>, tags: List<String> = listOf(), @Restrict restrict: String = Restrict.PUBLIC, edit: Boolean = false): Disposable? {
        if (loadState.get() !is LoadState.Loading) {
            var star = !illust.is_bookmarked
            if (edit) star = true
            val ob = if (illust.type == NOVEL) {
                instance
                        .starNovel(illust.id.toString(), star, tags, restrict)
            }else {
                instance
                        .starIllust(illust.id.toString(), star, tags, restrict)
            }
            return  ob
                    .doOnSubscribe { loadState.set(LoadState.Loading) }
                    .subscribeBy(onNext = {
                        illust.is_bookmarked = star
                        loadState.set(LoadState.Succeed)
                    }, onError = {
                        loadState.set(LoadState.Failed(it))
                    })
        }
        return null
    }

    /**
     * 添加书签
     */
    suspend fun addNovelMarker(novelId: String, page: Int): Any {
        return service.markNovel(novelId, page)
    }

    suspend fun deleteNovelMarker(novelId: String): Any {
        return service.unMarkNovel(novelId)
    }

    /**
     * 保存浏览记录
     */
    suspend fun saveHistory(illust: Illust) {
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtil.bean2Json(illust))
        return service.saveHistory(body)
    }

    /**
     * 获取浏览记录
     */
    @Throws(IOException::class)
    suspend fun getBrowserHistory(@IllustCategory category: String): IllustListResp {
        UserRepository.instance.loginData?.let {
            return service.getBrowserHistory(it.user.id, category)
        }
        return IllustListResp()
    }



    /**
     * 加载更多
     */
    fun loadMore(nextUrl: String): Observable<IllustListResp> {
        
        return service.getMoreIllust(nextUrl).doOnNext {
//                    with(it) {
//                        illusts.forEach {
//                            illust ->
//                            illustList[illust.id.toString()] = illust
//                        }
//                    }
                }
                .schedulerTransform()
    }

    fun loadMore(scope: CoroutineScope, nextUrl: String, loadState: ObservableField<LoadState>? = null, then: ((IllustListResp) -> Unit)? = null) {
        scope.ioTask(loadState, then = then) {
            service.getMoreIllust(nextUrl).awaitSingle()
        }
    }

}

@Throws(ApiException::class)
fun Observable<IllustListResp>.checkEmpty(): Observable<IllustListResp> = this.map {
    if (it.illusts.isEmpty()) {
        throw ApiException(ApiException.CODE_EMPTY_DATA)
    }
    it
}

@Throws(ApiException::class)
fun IllustListResp.checkEmpty() {
    if (this.illusts.isEmpty()) {
        throw ApiException(ApiException.CODE_EMPTY_DATA)
    }
}

/**
 * 是否可见
 */
@Throws(ApiException::class)
fun Illust.checkVisible() {
    if (!this.visible) {
        throw ApiException(ApiException.CODE_NOT_VISIBLE)
    }
}

