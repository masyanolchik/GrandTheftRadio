package com.example.anothertryofmusicanim.radiochooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anothertryofmusicanim.R
import com.example.anothertryofmusicanim.adapter.GameAdapter
import com.example.anothertryofmusicanim.adapter.RadioClickListener
import com.example.anothertryofmusicanim.databinding.RadioChooserBinding
import com.example.anothertryofmusicanim.entities.Game
import com.example.anothertryofmusicanim.playerview.PlayerViewOnClickListener
import com.example.anothertryofmusicanim.repo.Repository
import kotlinx.serialization.ImplicitReflectionSerializer

class RadioChooserFragment: Fragment() {
    private lateinit var binding: RadioChooserBinding
    lateinit var games: List<Game>
    @ImplicitReflectionSerializer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.radio_chooser,container,false)

        val application = requireNotNull(this.activity).application

        //val viewModelFactory = RadioChooserViewModelFactory(application)

        val viewModel = RadioChooserViewModel(application)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this


        games = Repository.get(activity!!)

        binding.gameRV.layoutManager = LinearLayoutManager(activity)

        val adapter = GameAdapter(object : RadioClickListener {
            override fun onClick(view: View, radioPosition: Int, gamePosition: Int?) {
                binding.player.visibility = View.VISIBLE
                if (gamePosition != null) {
                    viewModel.selectGame(radioPosition,gamePosition)
                    binding.player.invokePlayButton()
                }
            }
        })

        binding.gameRV.adapter = adapter

        viewModel.games.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        viewModel.currentGame.observe(viewLifecycleOwner, Observer {
            binding.player.setArtistName(it.name)
        })

        viewModel.currentRadio.observe(viewLifecycleOwner, Observer {
            binding.player.setTrackName(it.name)
            it.pic?.let { it1 -> binding.player.setAlbumImage(it1) }
        })

        binding.player.setOnViewListener(object : PlayerViewOnClickListener{
            override fun nextButtonHandler() {
                viewModel.nextStation()
                viewModel.stopPlaying()
                playButtonHandler()
            }

            override fun prevButtonHandler() {
                viewModel.prevStation()
                viewModel.stopPlaying()
                playButtonHandler()
            }

            override fun playButtonHandler() {
                viewModel.playCurrent()
            }

        })

        return binding.root
    }

}