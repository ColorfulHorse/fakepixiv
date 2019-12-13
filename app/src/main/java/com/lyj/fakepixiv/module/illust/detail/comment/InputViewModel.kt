package com.lyj.fakepixiv.module.illust.detail.comment

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.data.source.remote.IllustExtRepository
import com.lyj.fakepixiv.app.utils.ToastUtil
import com.lyj.fakepixiv.module.illust.detail.items.CommentListViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author green sun
 *
 * @date 2019/12/9
 *
 * @desc 评论vm
 */
class InputViewModel(val parent: CommentListViewModel) : BaseViewModel() {


    enum class State {
        CLOSE, TEXT, EMOJI
    }

    // 原评论id 回复用
    @get:Bindable
    var source: Comment? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.source)
        }

    @get:Bindable
    var comment = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.comment)
    }

    val state = ObservableField(State.CLOSE)

    fun hide() {
        state.set(State.CLOSE)
    }

    fun changeState() {
        state.get()?.let {
            if (it != State.EMOJI) {
                state.set(State.EMOJI)
            }else {
                state.set(State.TEXT)
            }
        }
    }

    fun send() {
        launch(CoroutineExceptionHandler { _, err ->
            ToastUtil.showToast(R.string.comment_failed)
        }) {
            val sourceComment = source
            withContext(Dispatchers.IO) {
                val service = IllustExtRepository.instance
                        .service
                if (sourceComment != null) {
                    service.addComment(parent.illust.id, comment, sourceComment.id.toString())
                }else {
                    service.addComment(parent.illust.id, comment)
                }
            }
            val res = Comment(comment)
            if (sourceComment == null) {
                // 是评论
                parent.data.add(0, CommentViewModel(parent, res))
                parent.piece()
            }else {
                // 是回复
                val source = parent.data.first { it.data.id == sourceComment.id }.data
                source.has_replies = true
                parent.piece.first { it.data.id == sourceComment.id }.data.has_replies = true
                parent.showApplies(source, listOf(res))
            }
        }
    }
}