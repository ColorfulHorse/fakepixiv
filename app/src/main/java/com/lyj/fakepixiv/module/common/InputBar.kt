package com.lyj.fakepixiv.module.common

import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.GridLayoutManager
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.data.model.response.Emoji
import com.lyj.fakepixiv.databinding.CommentInputBarBinding
import com.lyj.fakepixiv.databinding.ItemEmojiBinding
import com.lyj.fakepixiv.module.illust.detail.comment.InputViewModel

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
    }
}