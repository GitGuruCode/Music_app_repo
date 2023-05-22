package com.example.vmusic

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vmusic.databinding.ActivityPlaylistBinding
import com.example.vmusic.databinding.CustomAddPlaylistDialogBinding
import com.example.vmusic.databinding.SmallpartplaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class PlaylistActivity : AppCompatActivity() {
    lateinit var playlistAdapter:PlaylistAdapter
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityPlaylistBinding
        var allMusicPlaylist:AllPlayList= AllPlayList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.playlistrcv.layoutManager= GridLayoutManager(this,4)
        playlistAdapter=PlaylistAdapter(this, allMusicPlaylist.allPlayList)
        binding.playlistrcv.adapter=playlistAdapter
        binding.playlistAdd.setOnClickListener{
            customDialog()
        }
        binding.playlistBackBtn.setOnClickListener { finish() }
    }
    private fun customDialog(){
        val customDialog= LayoutInflater.from(this@PlaylistActivity).inflate(R.layout.custom_add_playlist_dialog, binding.root,false)
        val binder=CustomAddPlaylistDialogBinding.bind(customDialog)
        val builder= MaterialAlertDialogBuilder(this)
        builder.setView(customDialog)
        builder.setTitle("Create Playlist")
            .setPositiveButton("ADD"){dialog,_->
                addPlaylist(binder.dialogPlaylistName.text.toString())
              dialog.dismiss()
            }.show()
    }
    private fun addPlaylist(playlistName:String) {
        var playListExist=false
        for(i in allMusicPlaylist.allPlayList){
            if(playlistName.equals(i.name)){playListExist=true;break}
        }
        if(playListExist){
            Toast.makeText(this,"PlayList Already Exist!!",Toast.LENGTH_SHORT).show()
        }
        else{
            val tempPlayList:SinglePlayList=SinglePlayList()
            tempPlayList.name=playlistName
            tempPlayList.playlist= ArrayList()
            val calendar=Calendar.getInstance().time
            val sdf=SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH)
            tempPlayList.createdOn=sdf.format(calendar)
            allMusicPlaylist.allPlayList.add(tempPlayList)
            playlistAdapter.refreshPlayList()
        }
    }
}