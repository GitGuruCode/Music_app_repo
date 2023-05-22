package com.example.vmusic

import android.content.Context
import android.media.MediaMetadataRetriever
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import java.io.File
import java.util.concurrent.TimeUnit

data class AllMusic(var id:String,var title:String,var album:String,var path:String,
                     var duration:Long=0,var artist:String,var artUri:String)
class SinglePlayList{
   lateinit var name:String
   lateinit var playlist:ArrayList<AllMusic>
    lateinit var createdOn:String
}
class AllPlayList{
   var allPlayList:ArrayList<SinglePlayList> = ArrayList()
}
fun formatDuration(duration: Long):String{
   val minutes=TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS)
   val seconds=TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS)-minutes*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES)
  return String.format("%02d:%02d",minutes,seconds)
}
fun getImgArt(path: String): ByteArray? {
    val retriever=MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}
fun setSongPosition(increment: Boolean){
    if(increment){
        if(PlayerActivity.realMusicList2.size-1== PlayerActivity.songPosition){
            PlayerActivity.songPosition =0
        }
        else ++PlayerActivity.songPosition
    }
    else if(0== PlayerActivity.songPosition){
        PlayerActivity.songPosition = PlayerActivity.realMusicList2.size-1
    }
    else --PlayerActivity.songPosition
}
fun exitApplication(){
    PlayerActivity.musicService!!.audioManager.abandonAudioFocus(PlayerActivity.musicService)
    PlayerActivity.musicService!!.stopForeground(true)
    PlayerActivity.musicService!!.mediaPlayer!!.release()
    PlayerActivity.musicService = null
}
fun isFavourites(id:String):Int{
     PlayerActivity.isFavourite=false
FavouriteActivity.favMusicList.forEachIndexed { index, allMusic ->
    if(id==allMusic.id){
         PlayerActivity.isFavourite=true
        return index
    }
}
    return -1
}
 fun initializeAttribute(context: Context) {
    val img:ArrayList<ShapeableImageView> =ArrayList()
    img.add(PlaylistDetails.binding.shapeableImageView9)
    img.add(PlaylistDetails.binding.shapeableImageView5)
    img.add(PlaylistDetails.binding.shapeableImageView7)
    img.add(PlaylistDetails.binding.shapeableImageView6)

     PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].playlist.forEachIndexed{index,_->
         if(index< img.size){
             Glide.with(context).load( PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].playlist[index].artUri).apply(RequestOptions().placeholder(R.drawable.ic_baseline_music_note_24).centerCrop())
                 .into(img[index])
         }
     }
    PlaylistDetails.binding.textView4.text=PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].name
    var td :Long=0
    for (i in  PlaylistActivity.allMusicPlaylist.allPlayList[PlaylistDetails.currentPlaylistPos].playlist){
        td+=i.duration
    }
    val str1 = "Total time: "
    val str2 = formatDuration(td)
    val concatenated = "$str1$str2 min."
    PlaylistDetails.binding.textView3.text = concatenated
}
fun ifSongDeletedStorage(playlist:ArrayList<AllMusic>):ArrayList<AllMusic>{
    playlist.forEachIndexed{index,music ->
        val file= File(music.path)
        if(!file.exists()) playlist.removeAt(index)
    }
    return playlist
}