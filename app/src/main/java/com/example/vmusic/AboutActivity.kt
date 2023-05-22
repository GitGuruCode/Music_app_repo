package com.example.vmusic

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vmusic.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    lateinit var binding:ActivityAboutBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="About"
        binding.txtAbout.text="This App is Developed By Vikas"

    }
}