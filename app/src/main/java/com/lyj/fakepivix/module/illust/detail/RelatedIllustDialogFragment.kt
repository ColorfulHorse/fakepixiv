package com.lyj.fakepivix.module.illust.detail

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BottomDialogFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.DialogRelatedBinding
import com.lyj.fakepivix.module.common.adapter.IllustAdapter
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/6/25
 *
 * @desc 相关作品dialog
 */
class RelatedIllustDialogFragment : BottomDialogFragment() {

    var mBinding: DialogRelatedBinding? = null
    var mViewModel: RelatedIllustDialogViewModel? = null
    var mAdapter = IllustAdapter(ObservableArrayList()).apply {
        addItemType(Illust.TYPE_ILLUST, R.layout.item_illust_related, BR.illust)
        addItemType(Illust.TYPE_COMIC, R.layout.item_illust_related, BR.illust)
        addItemType(Illust.TYPE_NOVEL, R.layout.item_illust_related, BR.illust)
    }

    companion object {
        fun newInstance() = RelatedIllustDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.dialog_related, null)
        mBinding = DataBindingUtil.bind(view)
        mBinding?.let {
            with(it) {
                close.setOnClickListener {
                    this@RelatedIllustDialogFragment.dismiss()
                }
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                mAdapter.bindToRecyclerView(recyclerView)
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .dividerWidth(5.dp2px(), 0)
                        .build())
            }
        }
        mViewModel?.let {
            mAdapter.setNewData(it.data)
        }
        return view
    }
}