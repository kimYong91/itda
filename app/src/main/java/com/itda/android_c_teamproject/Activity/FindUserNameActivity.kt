package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.RetrofitClient
import com.itda.android_c_teamproject.databinding.ActivityFindUserNameBinding

class FindUserNameActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindUserNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            buttonExit.setOnClickListener {
                startActivity(Intent(this@FindUserNameActivity, LoginActivity::class.java))
            }



        }
    }
}