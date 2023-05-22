package com.example.vmusic

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vmusic.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import java.io.File
import com.example.vmusic.MyAdapter
import androidx.appcompat.widget.SearchView
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var myAdapter: MyAdapter
    var actionBarDrawerToggle: ActionBarDrawerToggle?=null
    companion object{
        lateinit var realMusicList: ArrayList<AllMusic>
        @SuppressLint("StaticFieldLeak")
        lateinit var binding:ActivityMainBinding
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestRuntimePermission()
          realMusicList=getAllAudio()
        binding.rcv.layoutManager=LinearLayoutManager(this)
        myAdapter=MyAdapter(this, realMusicList)
        binding.rcv.adapter=myAdapter
        binding.totalSongs.setText("Total songs : "+myAdapter.itemCount)
        actionBarDrawerToggle= ActionBarDrawerToggle(this,binding.mac,R.string.open,R.string.close)
        binding.mac.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.myNevigationView.setNavigationItemSelectedListener(this)
        binding.shuffle.setOnClickListener{
            val intent=Intent(this,PlayerActivity::class.java)
             intent.putExtra("position",0)
             intent.putExtra("class","MainActivity")
            startActivity(intent)
        }
        binding.favourite.setOnClickListener { startActivity(Intent(this,FavouriteActivity::class.java)) }
        binding.addPlaylist.setOnClickListener { startActivity(Intent(this,PlaylistActivity::class.java)) }
    }
    private fun requestRuntimePermission(){
         if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
             !=PackageManager.PERMISSION_GRANTED){
             ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),7)
         }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==7){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()

            }
            else ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),7)
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.fb->{
                startActivity(Intent(this,FeedbackActivity::class.java))
            }
            R.id.st->{
                startActivity(Intent(this,SettingActivity::class.java))
            }
            R.id.ab->{
                startActivity(Intent(this,AboutActivity::class.java))
            }
            R.id.ex->{
                val builder=MaterialAlertDialogBuilder(this)
                    builder.setTitle("Exit").setMessage("Do you want to close App?")
                        .setPositiveButton("Yes"){_,_->
                            if(PlayerActivity.musicService!=null){
                            exitApplication()
                            }
                            exitProcess(1)
                        }
                        .setNegativeButton("No"){dialog,_->
                            dialog.dismiss()
                        }
                val customDialog=builder.create()
                customDialog.show()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(actionBarDrawerToggle!!.onOptionsItemSelected(item)) return true
        else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(binding.mac.isDrawerOpen(GravityCompat.START)){
            binding.mac.close()
        }
        else super.onBackPressed()
    }
    @SuppressLint("Range")
    private fun getAllAudio():ArrayList<AllMusic>{
        var tempMusicList=ArrayList<AllMusic>()
        val selection=MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val projection= arrayOf(MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.DATE_ADDED
            ,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.ALBUM_ID)
        val cursor=this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,selection,null,
            MediaStore.Audio.Media.DATE_ADDED + " DESC",null)
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do {
                    val titlec=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val idc=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val albumc=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val pathc=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val artistc=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val albumidc=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val durationc=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val uri= Uri.parse("content://media/external/audio/albumart")
                    val artUric=Uri.withAppendedPath(uri,albumidc).toString()
                    val music=AllMusic(idc,titlec,albumc,pathc,durationc,artistc,artUric)
                    val file=File(music.path)
                    if(file.exists()){
                        tempMusicList.add(music)
                    }
                }while (cursor.moveToNext())
                cursor.close()
            }
        }
        return tempMusicList
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        val item=menu?.findItem(R.id.searchmenu)
        val searchView=item?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint="Search Songs..."
        searchView.setOnQueryTextListener(object :OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean =true
            override fun onQueryTextChange(p0: String?): Boolean {
                notesFiltering(p0)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun notesFiltering(p0: String?) {
        val newFilteredList= ArrayList<AllMusic>()
        for (i in realMusicList){
            if(i.title.contains(p0!!) || i.album.contains(p0)){
                newFilteredList.add(i)
            }
        }
        myAdapter.filtering(newFilteredList)
    }

}