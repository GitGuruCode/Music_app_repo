package com.example.vmusic

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore.Audio
import android.util.Log
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vmusic.databinding.ActivityPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class PlayerActivity : AppCompatActivity() ,ServiceConnection,MediaPlayer.OnCompletionListener{
    companion object{
        lateinit var realMusicList2:ArrayList<AllMusic>
        var songPosition=0
      //  var mediaPlayer:MediaPlayer?=null
        var isPlaying:Boolean=false
        var isLoop:Boolean=false
        var min5s:Boolean=false
        var min10s:Boolean=false
        var min15s:Boolean=false
        var songId:String=""
        var musicService:MusicService?=null
        @SuppressLint("StaticFieldLeak")
         lateinit var binding: ActivityPlayerBinding
         var isFavourite:Boolean=false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        // for starting service
        val intentp= Intent(this,MusicService::class.java)
        bindService(intentp,this, BIND_AUTO_CREATE)
        startService(intentp)
        songPosition=intent.getIntExtra("songPosition",0)
        startMedia()
        binding.playpause.setOnClickListener{
            if(isPlaying) pauseMusic()
            else playMusic()
        }
        binding.prevbtn.setOnClickListener{
            playNextPrev(false)
        }
        binding.nextbtn.setOnClickListener{
            playNextPrev(true)
        }

        binding.mySeekBar.setOnSeekBarChangeListener(object :OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, prgress: Int, fromUser: Boolean) {
                if(fromUser){ musicService!!.mediaPlayer!!.seekTo(prgress)}
            }
            override fun onStartTrackingTouch(p0: SeekBar?) =Unit
            override fun onStopTrackingTouch(p0: SeekBar?) =Unit
        })

        binding.loopbtn.setOnClickListener{ if(isLoop) {isLoop=false;binding.loopbtn.setColorFilter(ContextCompat.getColor(this,R.color.white))}
        else {isLoop=true;binding.loopbtn.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))}}

        binding.backbtn.setOnClickListener{finishActivity(1);finish()}
        binding.equilizer.setOnClickListener{
            val intent=Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, musicService!!.mediaPlayer!!.audioSessionId)
            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME,baseContext.packageName)
            intent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE,AudioEffect.CONTENT_TYPE_MUSIC)
            startActivityForResult(intent,7)
        }
        binding.timerbtn.setOnClickListener{
            if(min5s || min10s || min15s){
                val builder= MaterialAlertDialogBuilder(this)
                builder.setTitle("Timer").setMessage("Do you want to close Timer?")
                    .setPositiveButton("Yes"){_,_->
                        min5s=false
                        min10s=false
                        min15s=false
                        binding.timerbtn.setColorFilter(ContextCompat.getColor(this,R.color.white))
                    }
                    .setNegativeButton("No"){dialog,_->
                        dialog.dismiss()
                    }
                val customDialog=builder.create()
                customDialog.show()
                customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            }
           else showBottomSheetDialog()
        }
        binding.sharebtn.setOnClickListener {
            val intent=Intent()
            intent.action=Intent.ACTION_SEND
            intent.type="audio/*"
            intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(realMusicList2[songPosition].path))
            startActivity(Intent.createChooser(intent,"Sharing Music File"))
        }
        binding.pafav.setOnClickListener {
            if(isFavourites(realMusicList2[songPosition].id)!=-1){
                isFavourite=false
                FavouriteActivity.favMusicList.removeAt(isFavourites(realMusicList2[songPosition].id))
                binding.pafav.setColorFilter(ContextCompat.getColor(this,R.color.white))
                NowPlayingFragment.binding.npfav.setColorFilter(ContextCompat.getColor(this,R.color.white))
            }
            else{
                binding.pafav.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
                NowPlayingFragment.binding.npfav.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
              //  FavouriteActivity.sIndex=isFavourites(realMusicList2[songPosition].id)
                FavouriteActivity.favMusicList.add(realMusicList2[songPosition])
                isFavourite=true
            }
        }
        Log.d("@@@@", "startService ")
    }
    private fun showBottomSheetDialog() {
        val dialog=BottomSheetDialog(this@PlayerActivity)
        dialog.setContentView(R.layout.bottom_sheet_dialog)
        dialog.show()
        dialog.findViewById<TextView>(R.id.min5)?.setOnClickListener {
        Toast.makeText(baseContext,"Music will stop after 5 minutes",Toast.LENGTH_SHORT).show()
            binding.timerbtn.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
            min5s=true
            Thread{ Thread.sleep(10000)
           if(min5s){ exitApplication();exitProcess(1)}}
            .start()
            dialog.dismiss()

        }
        dialog.findViewById<TextView>(R.id.min10)?.setOnClickListener {
        Toast.makeText(baseContext,"Music will stop after 10 minutes",Toast.LENGTH_SHORT).show()
            binding.timerbtn.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
            min10s=true
            Thread{ Thread.sleep(10000)
                if(min10s){ exitApplication();exitProcess(1)}}
                .start()
            dialog.dismiss()

        }
        dialog.findViewById<TextView>(R.id.min15)?.setOnClickListener {
        Toast.makeText(baseContext,"Music will stop after 15 minutes",Toast.LENGTH_SHORT).show()
            binding.timerbtn.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
            min15s=true
            Thread{ Thread.sleep(10000)
                if(min15s){ exitApplication();exitProcess(1)}}
                .start()
            dialog.dismiss()

        }
    }

    private fun setPaImg(){
        // indexSong= isFavourites(realMusicList2[songPosition].id)
        Glide.with(this).load(realMusicList2[songPosition].artUri).apply(RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
        .into(binding.PAIMG)
        binding.PATITLE.text= realMusicList2[songPosition].title
        if(isLoop) binding.loopbtn.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
        if(min5s || min10s ||min15s) binding.timerbtn.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
        if(isFavourites(realMusicList2[songPosition].id)!=-1) binding.pafav.setColorFilter(ContextCompat.getColor(this,R.color.purple_500))
        else binding.pafav.setColorFilter(ContextCompat.getColor(this,R.color.white))
    }
   @RequiresApi(Build.VERSION_CODES.M)
   private fun playMusic(){
       Log.d("@@@@", "startServicegfhgfhfhfhfhfhfhfhfhgfhg ")
        binding.playpause.setIconResource(R.drawable.ic_baseline_pause_circle_filled_24)
        musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
        isPlaying=true
        musicService!!.mediaPlayer!!.start()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun pauseMusic(){
        binding.playpause.setIconResource(R.drawable.ic_baseline_play_circle_filled_24)
        musicService!!.showNotification(R.drawable.ic_baseline_play_circle_filled_24)
        isPlaying=false
        musicService!!.mediaPlayer!!.pause()
    }
    private fun startMedia(){
        when(intent.getStringExtra("class")){
            "NowPlayingFragment"->{
                setPaImg()
            }
            "MyAdapter"->{
                realMusicList2= ArrayList()
                realMusicList2.addAll(MainActivity.realMusicList)
                setPaImg()
             //  innerOfStartMedia()
            }
            "MainActivity"->{
                realMusicList2= ArrayList()
                realMusicList2.addAll(MainActivity.realMusicList)
                realMusicList2.shuffle()
                setPaImg()
              //  innerOfStartMedia()
            }
            "PlaylistDetailsAdapter"->{
                realMusicList2= ArrayList()
                realMusicList2.addAll( PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].playlist)
                setPaImg()
            }
            "PlaylistDetails"->{
                realMusicList2= ArrayList()
                realMusicList2.addAll( PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].playlist)
                realMusicList2.shuffle()
                setPaImg()
            }
            "FavAdapter"->{
                realMusicList2= ArrayList()
                realMusicList2.addAll(FavouriteActivity.favMusicList)
                setPaImg()
            }
            "FavouriteActivity"->{
                realMusicList2= ArrayList()
                realMusicList2.addAll(FavouriteActivity.favMusicList)
                realMusicList2.shuffle()
                setPaImg()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun innerOfStartMedia(){
        if(musicService!!.mediaPlayer==null) musicService!!.mediaPlayer= MediaPlayer()
        musicService!!.mediaPlayer!!.reset()
        musicService!!.mediaPlayer!!.setDataSource(realMusicList2[songPosition].path)
        musicService!!.mediaPlayer!!.prepare()
        musicService!!.mediaPlayer!!.start()
        isPlaying=true
        binding.playpause.setIconResource(R.drawable.ic_baseline_pause_circle_filled_24)
        binding.seekBarStartTv.text= formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
        binding.seekBarEndTv.text= formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
        binding.mySeekBar.progress=0
        binding.mySeekBar.max= musicService!!.mediaPlayer!!.duration
        songId= realMusicList2[PlayerActivity.songPosition].id
        musicService!!.mediaPlayer!!.setOnCompletionListener(this)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun playNextPrev(increment:Boolean){
        if(increment){
            setSongPosition(increment)
            startMedia()

            innerOfStartMedia()
            musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
        }
        else{
            setSongPosition(increment)
            startMedia()
            innerOfStartMedia()
            musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder=p1 as MusicService.MyBinder
        musicService=binder.currentService()
        startMedia()
        when(intent.getStringExtra("class")){
            "NowPlayingFragment"->{
                binding.seekBarStartTv.text= formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
                binding.seekBarEndTv.text= formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
                binding.mySeekBar.progress=musicService!!.mediaPlayer!!.currentPosition
                binding.mySeekBar.max= musicService!!.mediaPlayer!!.duration
            }
            else->{
                innerOfStartMedia()
            }
        }
        Log.d("@@@@", "onserviceConntecteefefwef")
        musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
        musicService!!.seekBarSetUp()
        musicService!!.audioManager=getSystemService(Context.AUDIO_SERVICE) as AudioManager
        musicService!!.audioManager.requestAudioFocus(musicService,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_LOSS)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
           musicService=null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCompletion(p0: MediaPlayer?) {
        if(!isLoop)setSongPosition(true)
        startMedia()
        innerOfStartMedia()
        musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==7 || resultCode== RESULT_OK){return}
    }
}
