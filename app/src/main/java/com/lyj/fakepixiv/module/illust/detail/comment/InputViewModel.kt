package com.lyj.fakepixiv.module.illust.detail.comment

import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.model.response.Comment
import com.lyj.fakepixiv.app.data.model.response.Emoji
import com.lyj.fakepixiv.app.data.model.response.EmojiResp
import com.lyj.fakepixiv.app.data.source.remote.IllustExtRepository
import com.lyj.fakepixiv.app.databinding.Dynamic
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.utils.EmojiUtil
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.app.utils.ToastUtil
import com.lyj.fakepixiv.module.illust.detail.items.CommentListViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * @author green sun
 *
 * @date 2019/12/9
 *
 * @desc 评论vm
 */
class InputViewModel(val parent: CommentListViewModel, var keyboardListener: ((Boolean) -> Unit)? = null) : BaseViewModel() {


    enum class State {
        CLOSE, TEXT, EMOJI
    }

    val maxLength = 140

    // 原评论id 回复用
    @get:Bindable
    var source: Comment? by Dynamic(null, BR.source)

    @get:Bindable
    var comment by Dynamic("", BR.comment)

    // 表情列表高度
    @get:Bindable
    var emojiHeight by Dynamic(746, BR.emojiHeight)

    // 显示表情界面
    val emojiShow = ObservableField(false)

    @get:Bindable
    var keyboardShow by Dynamic(false, BR.keyboardShow)

    val emojiList = ObservableArrayList<Emoji>()

    @get:Bindable
    var state by Dynamic(State.CLOSE, BR.state)

    init {
        val h = SPUtil.get(Constant.SP.KEY_KEYBOARD_HEIGHT)
        if (h != -1) {
            emojiHeight = h
        }
        val resp = SPUtil.getObj<EmojiResp>(Constant.SP.KEY_EMOJI)
        resp?.let {
            emojiList.addAll(it.emoji_definitions)
        }
    }

    fun keyboardChanged(isOpen: Boolean) {
        if (isOpen) {
            val h = SPUtil.get(Constant.SP.KEY_KEYBOARD_HEIGHT)
            if (h != -1) {
                emojiHeight = h
            }
            state = State.TEXT
            emojiShow.set(true)
        } else {
            if (state != State.EMOJI) {
                emojiShow.set(false)
                state = State.CLOSE
            }
        }
    }

    fun show() {
        state = State.TEXT
        keyboardShow = true
    }

    fun hide() {
        keyboardShow = false
        state = State.CLOSE
    }

    fun changeState() {
        if (state != State.EMOJI) {
            state = State.EMOJI
            keyboardShow = false
        } else {
            show()
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
                } else {
                    service.addComment(parent.illust.id, comment)
                }
            }
            val res = Comment(comment)
            if (sourceComment == null) {
                // 是评论
                parent.data.add(0, CommentViewModel(parent, res))
                parent.piece()
            } else {
                // 是回复
                val source = parent.data.first { it.data.id == sourceComment.id }.data
                source.has_replies = true
                parent.piece.first { it.data.id == sourceComment.id }.data.has_replies = true
                parent.showApplies(source, listOf(res))
            }
        }
    }
}