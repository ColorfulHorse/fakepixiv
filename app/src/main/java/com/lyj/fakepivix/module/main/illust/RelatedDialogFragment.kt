package com.lyj.fakepivix.module.main.illust

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.app.utils.screenHeight
import com.lyj.fakepivix.databinding.DialogRelatedBinding
import com.lyj.fakepivix.module.main.common.IllustListFragment
import com.lyj.fakepivix.module.main.common.adapter.IllustAdapter
import com.lyj.fakepivix.widget.CommonItemDecoration

/**
 * @author greensun
 *
 * @date 2019/6/25
 *
 * @desc 相关作品dialog
 */
class RelatedDialogFragment : DialogFragment() {

    var mBinding: DialogRelatedBinding? = null
    var mViewModel: RelatedDialogViewModel? = null
    set(value) {
        field = value
        mBinding?.let {
            it.vm = field
            mAdapter.setNewData(field?.data)
        }
    }
    var mAdapter = object : IllustAdapter(ObservableArrayList()){
        override fun initItemType() {
            addItemType(Illust.TYPE_ILLUST, R.layout.item_illust, BR.illust)
            addItemType(Illust.TYPE_COMIC, R.layout.item_illust, BR.illust)
            addItemType(Illust.TYPE_NOVEL, R.layout.item_illust, BR.illust)
        }
    }

    companion object {
        fun newInstance() = RelatedDialogFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.dialog_related, null)
        mBinding = DataBindingUtil.bind(view)
        mBinding?.let {
            with(it) {
                close.setOnClickListener {
                    this@RelatedDialogFragment.dismiss()
                }
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                mAdapter.bindToRecyclerView(recyclerView)
                recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                        .dividerWidth(5.dp2px(), 0)
                        .build())
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lp = dialog.window.attributes as WindowManager.LayoutParams
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = screenHeight()/3
        lp.dimAmount = 0.2f
        lp.windowAnimations = R.style.bottom_sheet_anim
    }
}