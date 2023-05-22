package com.example.vmusic

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vmusic.databinding.CustomAddPlaylistDialogBinding
import com.example.vmusic.databinding.SmallparfavBinding
import com.example.vmusic.databinding.SmallpartplaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlaylistAdapter(val context: Context, var allPlayList:ArrayList<SinglePlayList>)
    : RecyclerView.Adapter<PlaylistAdapter.UserViewHolder>(){
    class UserViewHolder(val binding: SmallpartplaylistBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(SmallpartplaylistBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       holder.binding.playlistTitle.text=allPlayList[position].name
        if(PlaylistActivity.allMusicPlaylist.allPlayList[position].playlist.size>0){
        Glide.with(context).load(PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].playlist[0].artUri).apply(RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
            .into(holder.binding.eachplaylistimg)}
        holder.binding.deleteplaylist.setOnClickListener {
            val builder= MaterialAlertDialogBuilder(context)
            builder.setTitle("Delete Playlist").setMessage("Do you want to delete Playlist?")
                .setPositiveButton("Yes"){dialog,_->
                  PlaylistActivity.allMusicPlaylist.allPlayList.removeAt(position)
                    refreshPlayList()
                    dialog.dismiss()
                }
                .setNegativeButton("No"){dialog,_->
                    dialog.dismiss()
                }
            val customDialog=builder.create()
            customDialog.show()
        }
        holder.binding.root.setOnClickListener{
            val intent=Intent(context,PlaylistDetails::class.java)
            intent.putExtra("playlistPosition",position)
            intent.putExtra("class","PlaylistAdapter")
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return allPlayList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun refreshPlayList(){
        allPlayList= ArrayList()
        allPlayList.addAll(PlaylistActivity.allMusicPlaylist.allPlayList)
        notifyDataSetChanged()
    }
}