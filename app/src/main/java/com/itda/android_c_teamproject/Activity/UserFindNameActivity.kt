package com.itda.android_c_teamproject.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itda.android_c_teamproject.databinding.ActivityFindUserNameBinding

class UserFindNameActivity : AppCompatActivity() {
    lateinit var binding: ActivityFindUserNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {
            buttonExit.setOnClickListener {
                startActivity(Intent(this@UserFindNameActivity, LoginActivity::class.java))
            }



        }
    }
}