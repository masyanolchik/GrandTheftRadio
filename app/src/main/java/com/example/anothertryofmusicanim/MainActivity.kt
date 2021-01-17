package com.example.anothertryofmusicanim

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anothertryofmusicanim.adapter.GameAdapter
import com.example.anothertryofmusicanim.adapter.RadioClickListener
import com.example.anothertryofmusicanim.entities.Game
import com.example.anothertryofmusicanim.entities.Radio
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}