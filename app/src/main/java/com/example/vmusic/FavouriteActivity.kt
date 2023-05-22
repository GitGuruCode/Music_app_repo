package com.example.vmusic

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vmusic.databinding.ActivityFavouriteBinding

class FavouriteActivity : AppCompatActivity() {
    lateinit var myAdapter:FavAdapter
    lateinit var binding:ActivityFavouriteBinding
    companion object{
        var favMusicList:ArrayList<AllMusic> =ArrayList()
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        favMusicList= ifSongDeletedStorage(favMusicList)
        binding.favrcv.layoutManager= GridLayoutManager(this,5)
        myAdapter=FavAdapter(this, favMusicList)
        binding.favrcv.adapter=myAdapter
           binding.FAshuffle.setOnClickListener{
            val intent= Intent(this,PlayerActivity::class.java)
            intent.putExtra("position",0)
            intent.putExtra("class","FavouriteActivity")
            startActivity(intent)
        }
     binding.Favbackbtn.setOnClickListener { finish() }
    }
}