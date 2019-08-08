package com.lyj.fakepivix.module.main.illust

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.base.BottomDialogFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.UserPreview
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.DialogRelatedBinding
import com.lyj.fakepivix.databinding.ItemUserRelatedBinding
import com.lyj.fakepivix.module.common.adapter.IllustAdapter
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/6/25
 *
 * @desc 相关作者dialog
 */
class RelatedUserDialogFragment : BottomDialogFragment() {

    var mBinding: DialogRelatedBinding? = null
    var mViewModel: RelatedUserDialogViewModel? = null
    var mAdapter = object : BaseBindingAdapter<UserPreview, ItemUserRelatedBinding>(R.layout.item_user_related, mutableListOf(), BR.data) {
        override fun convert(helper: BaseBindingViewHolder<ItemUserRelatedBinding>, item: UserPreview) {
            super.convert(helper, item)
            helper.addOnClickListener(R.id.follow)
            helper.binding?.let {
                it.recyclerView.layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
                var list = item.illusts
                if (list.size < 3) {
                    val novels = item.novels.take(3 - list.size)
                    novels.forEach { value -> value.type = IllustCategory.NOVEL }
                    list.addAll(novels)
                }
                var adapter = it.recyclerView.adapter
                if (adapter == null) {
                    adapter = IllustAdapter(list).apply {
                        addItemType(Illust.TYPE_ILLUST, R.layout.item_illust_small, BR.illust)
                        addItemType(Illust.TYPE_COMIC, R.layout.item_illust_small, BR.illust)
                        addItemType(Illust.TYPE_NOVEL, R.layout.item_illust_small, BR.illust)
                    }
                    adapter.bindToRecyclerView(it.recyclerView)
                    it.recyclerView.addItemDecoration(CommonItemDecoration.Builder().dividerWidth(1.dp2px(), 0).draw(false).build())
                }else {
                    (adapter as IllustAdapter).setNewData(item.illusts)
                }
            }
        }
    }

    companion object {
        fun newInstance() = RelatedUserDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.dialog_related, null)
        mBinding = DataBindingUtil.bind(view)
        mBinding?.let {
            with(it) {
                title.text = getString(R.string.title_related_user)
                close.setOnClickListener {
                    this@RelatedUserDialogFragment.dismiss()
                }
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                mAdapter.bindToRecyclerView(recyclerView)
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .dividerWidth(16.dp2px(), 0)
                        .build())
                mAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
                    when (view.id) {
                        R.id.follow -> {
                            mViewModel?.follow(position)
                        }
                    }
                }
            }
        }
        mViewModel?.let {
            mAdapter.setNewData(it.data)
        }
        return view
    }
}