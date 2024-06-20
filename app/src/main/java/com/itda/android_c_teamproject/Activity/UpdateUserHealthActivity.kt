package com.itda.android_c_teamproject.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.databinding.ActivityUpdateUserHealthBinding

class UpdateUserHealthActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateUserHealthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserHealthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {

        }

    }
}