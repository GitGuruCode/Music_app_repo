package com.example.vmusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vmusic.databinding.FragmentNowPlayingBinding
import kotlin.system.exitProcess


class NotificationRecevier:BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(p0: Context?, p1: Intent?) {
        when (p1!!.action) {
            ApplicationClass.PREVIOUS -> prevNextSong(false, p0!!)
            ApplicationClass.PLAY -> {if (PlayerActivity.isPlaying) pauseMusic() else playMusic()}
            ApplicationClass.NEXT -> prevNextSong(true,p0!!)
            ApplicationClass.EXIT -> {
                    exitApplication()
                exitProcess(1)
            }
        }
    }
        @RequiresApi(Build.VERSION_CODES.M)
      private fun playMusic(){
            PlayerActivity.isPlaying=true
            PlayerActivity.musicService!!.mediaPlayer!!.start()
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
            PlayerActivity.binding.playpause.setIconResource(R.drawable.ic_baseline_pause_circle_filled_24)
            NowPlayingFragment.binding.npbtn.setIconResource(R.drawable.ic_baseline_pause_circle_filled_24)
        }
        @RequiresApi(Build.VERSION_CODES.M)
      private fun pauseMusic(){
            PlayerActivity.isPlaying=false
            PlayerActivity.musicService!!.mediaPlayer!!.pause()
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_baseline_play_circle_filled_24)
            PlayerActivity.binding.playpause.setIconResource(R.drawable.ic_baseline_play_circle_filled_24)
            NowPlayingFragment.binding.npbtn.setIconResource(R.drawable.ic_baseline_play_circle_filled_24)
        }
   @RequiresApi(Build.VERSION_CODES.M)
   private  fun prevNextSong(increament:Boolean, context: Context){
       setSongPosition(increament)
       PlayerActivity.musicService!!.innerOfStartMedia()
       Glide.with(context).load(PlayerActivity.realMusicList2[PlayerActivity.songPosition].artUri).apply(
           RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
           .into(PlayerActivity.binding.PAIMG)
       PlayerActivity.binding.PATITLE.text= PlayerActivity.realMusicList2[PlayerActivity.songPosition].title
       Glide.with(context).load(PlayerActivity.realMusicList2[PlayerActivity.songPosition].artUri).apply(
           RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
           .into(NowPlayingFragment.binding.npImg)
       NowPlayingFragment.binding.nptitle.text= PlayerActivity.realMusicList2[PlayerActivity.songPosition].title
     //  PlayerActivity.indexSong= isFavourites(PlayerActivity.realMusicList2[PlayerActivity.songPosition].id)
if(isFavourites(PlayerActivity.realMusicList2[PlayerActivity.songPosition].id)!=-1){
    PlayerActivity.binding.pafav.setColorFilter(ContextCompat.getColor(context,R.color.purple_500))
}
else PlayerActivity.binding.pafav.setColorFilter(ContextCompat.getColor(context,R.color.white))
       playMusic()
     }
    }
