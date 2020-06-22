package com.lhd.view.basewithme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.base.baselibrary.activity.BaseActivity
import com.lhd.view.basewithme.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}
