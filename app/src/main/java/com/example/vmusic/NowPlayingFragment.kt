package com.example.vmusic

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vmusic.databinding.FragmentNowPlayingBinding

class NowPlayingFragment : Fragment() {
     companion object{
         @SuppressLint("StaticFieldLeak")
         lateinit var binding:FragmentNowPlayingBinding
     }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentNowPlayingBinding.inflate(layoutInflater)
        //val view=inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding.root.visibility=View.INVISIBLE
        MainActivity.binding.fragmentContainerView.visibility=View.GONE
        binding.npbtn.setOnClickListener{
            if(PlayerActivity.isPlaying) pauseMusic() else playMusic()
        }
            binding.npfav.setOnClickListener {
                if(isFavourites(PlayerActivity.realMusicList2[PlayerActivity.songPosition].id)!=-1){
                    PlayerActivity.isFavourite =false
                    FavouriteActivity.favMusicList.removeAt(isFavourites(PlayerActivity.realMusicList2[PlayerActivity.songPosition].id))
                    PlayerActivity.binding.pafav.setColorFilter(ContextCompat.getColor(requireContext(),R.color.white))
                    binding.npfav.setColorFilter(ContextCompat.getColor(requireContext(),R.color.white))

                }
                else{
                    PlayerActivity.binding.pafav.setColorFilter(ContextCompat.getColor(requireContext(),R.color.purple_500))
                   binding.npfav.setColorFilter(ContextCompat.getColor(requireContext(),R.color.purple_500))
                    //  FavouriteActivity.sIndex=isFavourites(realMusicList2[songPosition].id)
                    FavouriteActivity.favMusicList.add(PlayerActivity.realMusicList2[PlayerActivity.songPosition])
                    PlayerActivity.isFavourite =true
                }
            }
        binding.root.setOnClickListener{
            val intent= Intent(requireContext(),PlayerActivity::class.java)
            intent.putExtra("songPosition",PlayerActivity.songPosition)
            intent.putExtra("class","NowPlayingFragment")
            context?.startActivity(intent)
        }
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        if(PlayerActivity.musicService!=null){
            binding.root.visibility=View.VISIBLE
            MainActivity.binding.fragmentContainerView.visibility=View.VISIBLE
            binding.nptitle.isSelected=true
            Glide.with(this).load(PlayerActivity.realMusicList2[PlayerActivity.songPosition].artUri).apply(
                RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
                .into(binding.npImg)
            binding.nptitle.text= PlayerActivity.realMusicList2[PlayerActivity.songPosition].title
            if(PlayerActivity.isPlaying) binding.npbtn.setIconResource(R.drawable.ic_baseline_pause_circle_filled_24)
            else binding.npbtn.setIconResource(R.drawable.ic_baseline_play_circle_filled_24)

        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun playMusic(){
        PlayerActivity.isPlaying=true
        PlayerActivity.musicService!!.mediaPlayer!!.start()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
        PlayerActivity.binding.playpause.setIconResource(R.drawable.ic_baseline_pause_circle_filled_24)
        binding.npbtn.setIconResource(R.drawable.ic_baseline_pause_circle_filled_24)
        PlayerActivity.isPlaying=true
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun pauseMusic(){
        PlayerActivity.isPlaying=false
        PlayerActivity.musicService!!.mediaPlayer!!.pause()
        PlayerActivity.musicService!!.showNotification(R.drawable.ic_baseline_play_circle_filled_24)
        PlayerActivity.binding.playpause.setIconResource(R.drawable.ic_baseline_play_circle_filled_24)
        binding.npbtn.setIconResource(R.drawable.ic_baseline_play_circle_filled_24)
        PlayerActivity.isPlaying=false
    }
}