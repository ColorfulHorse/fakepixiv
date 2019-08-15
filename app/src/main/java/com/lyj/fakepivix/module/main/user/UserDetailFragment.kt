package com.lyj.fakepivix.module.main.user

import android.databinding.ViewDataBinding
import android.os.Bundle
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.data.model.response.User
import com.lyj.fakepivix.module.main.illust.IllustDetailFragment

/**
 * @author greensun
 *
 * @date 2019/3/20
 *
 * @desc
 */
class UserDetailFragment : FragmentationFragment<ViewDataBinding, UserDetailViewModel>() {

    override var mViewModel: UserDetailViewModel = UserDetailViewModel()

    private var userId: String = ""

    companion object {
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        fun newInstance(userId: String): IllustDetailFragment {
            return IllustDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_USER_ID, userId)
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            userId = it.getString(EXTRA_USER_ID, "")
            mViewModel.userId = userId
        }

        with(mBinding) {

        }
    }

    override fun bindLayout(): Int = R.layout.fragment_user_detail
}