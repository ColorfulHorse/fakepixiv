package com.lyj.fakepixiv.module.sample

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
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
        tablayout.apply {
            addTab(newTab().setText("111"))
            addTab(newTab().setText("222"))
            addTab(newTab().setText("333"))
        }
    }
}