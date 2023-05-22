package com.example.vmusic

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vmusic.databinding.SmallparfavBinding

class FavAdapter(val context: Context, var musicList:ArrayList<AllMusic>)
    : RecyclerView.Adapter<FavAdapter.UserViewHolder>(){
    class UserViewHolder(val binding: SmallparfavBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(SmallparfavBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.favsmallTitle.text=musicList[position].title
        Glide.with(context).load(musicList[position].artUri).apply(RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
            .into(holder.binding.favsmallImg)
        holder.binding.root.setOnClickListener{
            val intent= Intent(context,PlayerActivity::class.java)
            intent.putExtra("songPosition",position)
            intent.putExtra("class","FavAdapter")
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return musicList.size
    }
}