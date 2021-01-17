package com.example.anothertryofmusicanim.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.anothertryofmusicanim.R
import com.example.anothertryofmusicanim.databinding.RadioLayoutBinding
import com.example.anothertryofmusicanim.entities.Radio

class RadioAdapter (private val list: List<Radio>, private val clickListener: RadioClickListener, private val gamePosition: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RadioViewHolder.from(parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RadioViewHolder -> {
                val radioItem = list.get(position)
                holder.bind(clickListener,radioItem,gamePosition)
            }
        }
    }


    class RadioViewHolder(val binding: RadioLayoutBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        lateinit var clickListener: RadioClickListener
        var gamePosition:Int = 0

        fun bind(radioClickListener: RadioClickListener,radio: Radio, gamePosition: Int){
            this.clickListener = radioClickListener
            itemView.setOnClickListener(this)

            this.gamePosition = gamePosition

            binding.radio = radio
            //binding.radioImage.setImageResource()
        }

        override fun onClick(v: View?) {
            if (v != null) {
                clickListener?.onClick(v,adapterPosition,gamePosition)
            }
        }

        companion object{
            fun from(parent: ViewGroup): RecyclerView.ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RadioLayoutBinding.inflate(layoutInflater,parent,false)
                return RadioViewHolder(binding)
            }
        }
    }
}
interface RadioClickListener{
    fun onClick(view: View, radioPosition: Int, gamePosition: Int?)
}
