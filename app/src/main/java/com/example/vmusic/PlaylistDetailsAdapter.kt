package com.example.vmusic

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vmusic.databinding.SmallpartplaylistdetailsBinding

class PlaylistDetailsAdapter(val context: Context, var saareGaane:ArrayList<AllMusic>)
    : RecyclerView.Adapter<PlaylistDetailsAdapter.UserViewHolder>(){
    class UserViewHolder(val binding: SmallpartplaylistdetailsBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(SmallpartplaylistdetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.songTitle.text=saareGaane[position].title
        holder.binding.songAlbum.text=saareGaane[position].album
        holder.binding.songDuration.text= formatDuration(saareGaane[position].duration)
        Glide.with(context).load(saareGaane[position].artUri).apply(RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
            .into(holder.binding.songImage)
        holder.binding.root.setOnClickListener{
            val intent=Intent(context,PlayerActivity::class.java)
            intent.putExtra("songPosition",position)
            intent.putExtra("class","PlaylistDetailsAdapter")
            context.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {
        return saareGaane.size
    }
//    @SuppressLint("NotifyDataSetChanged")
//    fun refreshPlayList(){
//        allPlayList= ArrayList()
//        allPlayList.addAll(PlaylistActivity.allMusicPlaylist.allPlayList)
//        notifyDataSetChanged()
//    }
}