package com.lyj.fakepixiv.module.setting.account

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.databinding.FragmentAccountSettingBinding

/**
 * @author green sun
 *
 * @date 2019/11/20
 *
 * @desc
 */
class AccountFragment : BackFragment<FragmentAccountSettingBinding, AccountViewModel>() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    override var mViewModel: AccountViewModel = AccountViewModel()

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.contentView)
                .statusBarColor(R.color.transparent)
                .statusBarColorTransform(R.color.black)
                .statusBarAlpha(0.25f)
                .setOnKeyboardListener(keyboardListener)
                .init()
    }

    override fun bindBackIcon(): Drawable  = createDefaultBack()

    override fun bindLayout(): Int = R.layout.fragment_account_setting
}