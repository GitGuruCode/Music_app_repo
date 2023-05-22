package com.example.vmusic

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vmusic.databinding.SmallpartBinding

class AddSongAdapter(val context: Context, var musicList:ArrayList<AllMusic>)
    : RecyclerView.Adapter<AddSongAdapter.UserViewHolder>(){
    class UserViewHolder(val binding: SmallpartBinding): RecyclerView.ViewHolder(binding.root)
    fun filtering(filteredList:ArrayList<AllMusic>){
        musicList=filteredList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(SmallpartBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.songTitle.text=musicList[position].title
        holder.binding.songAlbum.text=musicList[position].album
        holder.binding.songDuration.text= formatDuration(musicList[position].duration)
        Glide.with(context).load(musicList[position].artUri).apply(RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
            .into(holder.binding.songImage)
        holder.binding.root.setOnClickListener{
            if(addSong(musicList[position])){
                holder.binding.root.setBackgroundColor(Color.GREEN)

            }
           else{
               holder.binding.root.setBackgroundColor(Color.CYAN)

           }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun addSong(allMusic: AllMusic): Boolean {
        PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].playlist.
        forEachIndexed{ index, song ->
            if(allMusic.id==song.id){
                PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].playlist.removeAt(index)
                PlaylistDetails.playlistAdapter.notifyDataSetChanged()
                initializeAttribute(context)
                return false
            }
        }
        PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].playlist.add(allMusic)
        PlaylistDetails.playlistAdapter.notifyDataSetChanged()
        initializeAttribute(context)
          return true
    }
    override fun getItemCount(): Int {
        return musicList.size
    }
}