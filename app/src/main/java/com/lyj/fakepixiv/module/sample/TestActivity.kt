package com.lyj.fakepixiv.module.sample

import android.os.Bundle
import android.view.WindowManager
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.R
import kotlinx.android.synthetic.main.activity_test.*

/**
 * @author green sun
 *
 * @date 2019/11/8
 *
 * @desc
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }
}