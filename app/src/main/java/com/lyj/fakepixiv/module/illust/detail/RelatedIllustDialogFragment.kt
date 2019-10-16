package com.lyj.fakepixiv.module.illust.detail

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BottomDialogFragment
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.DialogRelatedBinding
import com.lyj.fakepixiv.module.common.adapter.IllustAdapter
import com.lyj.fakepixiv.widget.CommonItemDecoration

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