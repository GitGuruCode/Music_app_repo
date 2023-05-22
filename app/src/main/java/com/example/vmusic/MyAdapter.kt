package com.example.vmusic
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vmusic.databinding.SmallpartBinding

class MyAdapter(val context: Context, var musicList:ArrayList<AllMusic>)
    : RecyclerView.Adapter<MyAdapter.UserViewHolder>(){
    class UserViewHolder(val binding: SmallpartBinding): RecyclerView.ViewHolder(binding.root)
    fun filtering(filteredList:ArrayList<AllMusic>){
        musicList=filteredList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(SmallpartBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
                    holder.binding.songTitle.text=musicList[position].title
                    holder.binding.songAlbum.text=musicList[position].album
                    holder.binding.songDuration.text= formatDuration(musicList[position].duration)
 Glide.with(context).load(musicList[position].artUri).apply(RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
            .into(holder.binding.songImage)
        holder.binding.root.setOnClickListener{
            val intent=Intent(context,PlayerActivity::class.java)
            intent.putExtra("songPosition",position)
            intent.putExtra("class","MyAdapter")
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return musicList.size
    }
}