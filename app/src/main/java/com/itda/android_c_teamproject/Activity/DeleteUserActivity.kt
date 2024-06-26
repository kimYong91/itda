package com.itda.android_c_teamproject.Activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itda.android_c_teamproject.R
import com.itda.android_c_teamproject.databinding.ActivityDeleteUserBinding
import com.itda.android_c_teamproject.network.RetrofitClient

class DeleteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteUserBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)
        val token = getToken()
        val username = sharedPreferences.getString("username", "") ?: ""
//
//        binding.run {
//            textDelete.setOnClickListener {
//                RetrofitClient.api.userDelete("Bearer ${token}", username).enqueu
//
//            }
//        }

    }
    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("app_pref", MODE_PRIVATE)
        return sharedPreferences.getString("token", null) ?: ""
    }
}