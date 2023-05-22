package com.example.vmusic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vmusic.databinding.ActivityFeedbackBinding
import com.example.vmusic.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    lateinit var binding:ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="Settings"
    }
}