package com.lyj.fakepixiv.module.illust.detail.comment

import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.GridLayoutManager
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.data.model.response.Emoji
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.app.utils.onKeyboardChanged
import com.lyj.fakepixiv.databinding.CommentInputBarBinding
import com.lyj.fakepixiv.databinding.ItemEmojiBinding

/**
 * @author green sun
 *
 * @date 2019/12/25
 *
 * @desc
 */
class InputBar(val binding: CommentInputBarBinding, val viewModel: InputViewModel) {


    init {
        val adapter = BaseBindingAdapter<Emoji, ItemEmojiBinding>(R.layout.item_emoji, viewModel.emojiList, BR.data)
        binding.recyclerView.layoutManager = GridLayoutManager(binding.root.context, 7)
        adapter.bindToRecyclerView(binding.recyclerView)
        adapter.setOnItemClickListener { _, _, position ->
            val emoji = "(${adapter.data[position].slug})"
            val length = binding.commentEditText.length()
            if (length + emoji.length <= viewModel.maxLength) {
                viewModel.comment = "${viewModel.comment}$emoji"
            }
        }
        binding.root.onKeyboardChanged { isOpen ->
            if (!isOpen) {
                viewModel.keyboardShow = false
                if (viewModel.state == InputViewModel.State.TEXT) {
                    viewModel.state = InputViewModel.State.CLOSE
                }
            }else {
                // 从表情键盘切换为文字键盘
                if (viewModel.state == InputViewModel.State.EMOJI) {
                    viewModel.emojiShow = false
                }
                viewModel.state = InputViewModel.State.TEXT
            }
            unlock()
        }
        binding.toggle.setOnClickListener {
            if (viewModel.state != InputViewModel.State.EMOJI) {
                showEmoji()
            }else {
                showText()
            }
        }
        binding.commentEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (viewModel.state == InputViewModel.State.EMOJI) {
                    showText()
                }
            }
            false
        }
    }

    fun showText() {
        lock()
        //viewModel.state = InputViewModel.State.TEXT
        viewModel.keyboardShow = true
    }

    fun showEmoji() {
        if (viewModel.state != InputViewModel.State.CLOSE) {
            lock()
            viewModel.keyboardShow = false
        }
        viewModel.state = InputViewModel.State.EMOJI
        val h = SPUtil.get(Constant.SP.KEY_KEYBOARD_HEIGHT, -1)
        if (h != -1) {
            viewModel.emojiHeight = h
        }
        viewModel.emojiShow = true
    }

    private fun lock() {
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, binding.dummy.height)
        lp.weight = 0f
        binding.dummy.layoutParams = lp
    }

    fun unlock() {
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
        lp.weight = 1f
        binding.dummy.layoutParams = lp
    }
}