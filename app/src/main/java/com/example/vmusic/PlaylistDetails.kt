package com.example.vmusic

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vmusic.databinding.ActivityPlaylistDetailsBinding
import com.example.vmusic.databinding.SmallpartplaylistdetailsBinding

class PlaylistDetails : AppCompatActivity() {
    private lateinit var binding2:SmallpartplaylistdetailsBinding
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var playlistAdapter: PlaylistDetailsAdapter
        @SuppressLint("StaticFieldLeak")
        lateinit var binding:ActivityPlaylistDetailsBinding
        var currentPlaylistPos=-1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPlaylistDetailsBinding.inflate(layoutInflater)
        binding2=SmallpartplaylistdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentPlaylistPos=intent.getIntExtra("playlistPosition",0)
        binding.addSongbtn.setOnClickListener{
            startActivity(Intent(this,AddSongActivity::class.java))

        }
        PlaylistActivity.allMusicPlaylist.allPlayList[currentPlaylistPos].playlist=
            ifSongDeletedStorage(PlaylistActivity.allMusicPlaylist.allPlayList[currentPlaylistPos].playlist)
        binding.PDrcv.layoutManager=LinearLayoutManager(this)
        playlistAdapter=PlaylistDetailsAdapter(this, PlaylistActivity.allMusicPlaylist.allPlayList[currentPlaylistPos].playlist)
        binding.PDrcv.adapter=playlistAdapter
        supportActionBar!!.hide()
        binding.backbtnPD.setOnClickListener { finish() }
        initializeAttribute(this)

        binding.sbtn.setOnClickListener{
            val intent= Intent(this,PlayerActivity::class.java)
            intent.putExtra("position",0)
            intent.putExtra("class","PlaylistDetails")
            startActivity(intent)
        }
        binding2.removeSongBtn.setOnClickListener{

        }
    }

}