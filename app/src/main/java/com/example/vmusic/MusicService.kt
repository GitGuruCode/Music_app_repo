package com.example.vmusic

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class MusicService:Service(),AudioManager.OnAudioFocusChangeListener {
    private var myBinder=MyBinder()
    private lateinit var mediaSession: MediaSessionCompat
    var mediaPlayer:MediaPlayer?=null
    private lateinit var runnable:Runnable
    lateinit var audioManager: AudioManager
    override fun onBind(p0: Intent?): IBinder {
        mediaSession= MediaSessionCompat(baseContext,"Vikas Music")
        return myBinder
    }
    inner class MyBinder:Binder(){
        fun currentService():MusicService{
            return this@MusicService
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SuspiciousIndentation")
    fun showNotification(playPauseBtn:Int){
        val intent=Intent(baseContext,MainActivity::class.java)
//        intent.putExtra("songPosition",PlayerActivity.songPosition)
//        intent.putExtra("class","NowPlayingFragment")
        val contentIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE)
        val prevIntent=Intent(baseContext,NotificationRecevier::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent=PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_IMMUTABLE)

        val playIntent=Intent(baseContext,NotificationRecevier::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent=PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_IMMUTABLE)

        val nextIntent=Intent(baseContext,NotificationRecevier::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent=PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_IMMUTABLE)

        val exitIntent=Intent(baseContext,NotificationRecevier::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent=PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_IMMUTABLE)
      val imageArt= getImgArt(PlayerActivity.realMusicList2[PlayerActivity.songPosition].path)
       val image=if (imageArt!=null){
           BitmapFactory.decodeByteArray(imageArt,0,imageArt.size)
       }else{
           BitmapFactory.decodeResource(resources,R.drawable.music)
       }
      val notification=NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
          .setContentIntent(contentIntent)
          .setContentTitle(PlayerActivity.realMusicList2[PlayerActivity.songPosition].title)
          .setContentText(PlayerActivity.realMusicList2[PlayerActivity.songPosition].artist)
          .setSmallIcon(R.drawable.ic_baseline_music_note_24)
          .setLargeIcon(image)
          .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
          .setOnlyAlertOnce(true)
          .addAction(R.drawable.ic_baseline_skip_previous_24,"previous",prevPendingIntent)
          .addAction(playPauseBtn,"play",playPendingIntent)
          .addAction(R.drawable.ic_baseline_skip_next_24,"next",nextPendingIntent)
          .addAction(R.drawable.ic_baseline_exit_to_app_24,"exit",exitPendingIntent)
          .build()
          startForeground(7,notification)
    }
     @RequiresApi(Build.VERSION_CODES.M)
     fun innerOfStartMedia(){
        if(PlayerActivity.musicService!!.mediaPlayer==null) PlayerActivity.musicService!!.mediaPlayer= MediaPlayer()
        PlayerActivity.musicService!!.mediaPlayer!!.reset()
        PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.realMusicList2[PlayerActivity.songPosition].path)
        PlayerActivity.musicService!!.mediaPlayer!!.prepare()
         PlayerActivity.musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
         PlayerActivity.binding.seekBarStartTv.text= formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
         PlayerActivity.binding.seekBarEndTv.text= formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.duration.toLong())
         PlayerActivity.binding.mySeekBar.progress=0
         PlayerActivity.binding.mySeekBar.max= PlayerActivity.musicService!!.mediaPlayer!!.duration
         PlayerActivity.songId = PlayerActivity.realMusicList2[PlayerActivity.songPosition].id
    }
    fun seekBarSetUp(){
        runnable= Runnable {
            PlayerActivity.binding.seekBarStartTv.text= formatDuration(PlayerActivity.musicService!!.mediaPlayer!!.currentPosition.toLong())
            PlayerActivity.binding.mySeekBar.progress=mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable,200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onAudioFocusChange(p0: Int) {
          if(p0<=0){
              //pause music
              PlayerActivity.binding.playpause.setIconResource(R.drawable.ic_baseline_play_circle_filled_24)
              NowPlayingFragment.binding.npbtn.setIconResource(R.drawable.ic_baseline_play_circle_filled_24)
              PlayerActivity.musicService!!.showNotification(R.drawable.ic_baseline_play_circle_filled_24)
              PlayerActivity.isPlaying =false
              PlayerActivity.musicService!!.mediaPlayer!!.pause()
          }
        else {
            //play music
              PlayerActivity.binding.playpause.setIconResource(R.drawable.ic_baseline_pause_circle_filled_24)
              NowPlayingFragment.binding.npbtn.setIconResource(R.drawable.ic_baseline_pause_circle_filled_24)
              PlayerActivity.musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
              PlayerActivity.isPlaying =true
              PlayerActivity.musicService!!.mediaPlayer!!.start()
        }
    }
}