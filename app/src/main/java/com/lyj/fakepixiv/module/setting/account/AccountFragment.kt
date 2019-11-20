package com.lyj.fakepixiv.module.setting.account

import android.os.Bundle
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.databinding.FragmentAccountSettingBinding

/**
 * @author green sun
 *
 * @date 2019/11/20
 *
 * @desc
 */
class AccountFragment : FragmentationFragment<FragmentAccountSettingBinding, AccountViewModel>() {

    override var mViewModel: AccountViewModel = AccountViewModel()

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun bindLayout(): Int = R.layout.fragment_account_setting
}