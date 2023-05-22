package com.example.vmusic

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vmusic.databinding.ActivityAddSongBinding


class AddSongActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddSongBinding
    lateinit var myAdapter: AddSongAdapter
    lateinit var songlist:ArrayList<AllMusic>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddSongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.sh.queryHint="Search Songs"
        songlist=MainActivity.realMusicList
        binding.addrcv.layoutManager= LinearLayoutManager(this)
        myAdapter= AddSongAdapter(this, songlist)
        binding.addrcv.adapter=myAdapter
        binding.addBack.setOnClickListener{finish()}
        val searchView: androidx.appcompat.widget.SearchView = findViewById(R.id.sh)
        val searchEditText: EditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        binding.sh.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean =true
            override fun onQueryTextChange(p0: String?): Boolean {
                searchEditText.setTextColor(Color.WHITE)
                notesFiltering(p0)
                return true
            }
    })

}
  private fun notesFiltering(p0: String?){
      val newFilteredList= ArrayList<AllMusic>()
      for (i in MainActivity.realMusicList){
          if(i.title.contains(p0!!) || i.album.contains(p0)){
              newFilteredList.add(i)
          }
      }
      myAdapter.filtering(newFilteredList)
  }

}