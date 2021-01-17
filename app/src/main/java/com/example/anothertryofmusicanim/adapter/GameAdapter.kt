package com.example.anothertryofmusicanim.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anothertryofmusicanim.R
import com.example.anothertryofmusicanim.databinding.GameLayoutBinding
import com.example.anothertryofmusicanim.entities.Game

class GameAdapter (private val radioClickListener: RadioClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var list: List<Game> = ArrayList<Game>()

    fun submitList(games : List<Game>){
        list = games
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GameViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is GameViewHolder -> {
                val gameItem = list.get(position)
                holder.bind(gameItem,radioClickListener,position)
            }
        }
    }

    override fun getItemCount(): Int = list.size



    class GameViewHolder(val binding:GameLayoutBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(game : Game, radioClickListener: RadioClickListener, gamePosition: Int){
            binding.game = game
            val radioAdapter: RadioAdapter = RadioAdapter(game.stations,radioClickListener,gamePosition)
            binding.radioRV.layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.HORIZONTAL,false)
            binding.radioRV.adapter = radioAdapter

        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GameLayoutBinding.inflate(layoutInflater, parent, false)
                return GameViewHolder(binding)
            }
        }
    }

}
