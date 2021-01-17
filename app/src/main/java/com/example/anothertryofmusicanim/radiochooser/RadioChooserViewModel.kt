package com.example.anothertryofmusicanim.radiochooser

import android.annotation.SuppressLint
import android.app.Application
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.icu.util.TimeZone
import android.os.Build
import android.util.Log
import android.util.SparseArray
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.anothertryofmusicanim.GrandTheftRadioApplication
import com.example.anothertryofmusicanim.entities.Game
import com.example.anothertryofmusicanim.entities.Radio
import com.example.anothertryofmusicanim.radioplayer.ExoPlayerManager
import com.example.anothertryofmusicanim.repo.Repository
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import kotlinx.serialization.ImplicitReflectionSerializer

@ImplicitReflectionSerializer
class RadioChooserViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<GrandTheftRadioApplication>().applicationContext

    private var _games = MutableLiveData<List<Game>>()

    val games: LiveData<List<Game>>
        get() = _games

    private var _currentGame: MutableLiveData<Game> = MutableLiveData()
    val currentGame: LiveData<Game>
        get() = _currentGame

    private var _radios = MutableLiveData<List<Radio>>()

    private  var _currentRadio: MutableLiveData<Radio> =  MutableLiveData()
    val currentRadio: LiveData<Radio>
        get() = _currentRadio

    private var _currentRadioPosition = 0

    private val _player = ExoPlayerManager.getSharedInstance(context)

    var isPlaying = false

    init{
        initializeGames()
    }

    fun initializeGames() {
        _games.value = Repository.get(context)
        _currentGame.value = _games.value!![0]
        _radios.value = _currentGame.value!!.stations
        _currentRadio.value = _radios.value!![0]
        _currentRadioPosition = 0
    }

    fun selectGame(radioPosition: Int, gamePosition: Int){
        val newGame = _games.value!![gamePosition]
        if (newGame != _currentGame.value){
            _currentGame.value = _games.value!![gamePosition]
            _radios.value = _currentGame.value!!.stations
        }
        _currentRadio.value = _radios.value!![radioPosition]
        _currentRadioPosition = radioPosition
    }



    fun nextStation() {
        if(_currentRadioPosition + 1 != _radios.value!!.size){
            ++_currentRadioPosition
        } else {
            _currentRadioPosition = 0
        }
        _currentRadio.value = _radios.value!![_currentRadioPosition]
        //playCurrent()
    }

    fun prevStation() {
        if(_currentRadioPosition  != 0){
            --_currentRadioPosition
        } else {
            _currentRadioPosition = _radios.value!!.size - 1
        }
        _currentRadio.value = _radios.value!![_currentRadioPosition]
        //playCurrent()
    }

    fun stopPlaying(){
        //stops media player
        if(isPlaying){
            _player.stopPlayer(true)
            isPlaying = false
        }
    }

    fun playCurrent() {
        //start media player
        if(isPlaying){
            _player.stopPlayer(true)
            isPlaying = false
        } else{
            playStream(_currentRadio.value!!.link)
        }
    }

    private fun playStream(link: String) {
        @SuppressLint("StaticFieldLeak") val mExtractor: YouTubeExtractor =
            object : YouTubeExtractor(context) {
                override fun onExtractionComplete(
                    ytFiles: SparseArray<YtFile>,
                    vMeta: VideoMeta
                ) {
                    if (ytFiles != null) {
                        val itag = 251
                        val downloadUrl = ytFiles[itag].url
                        playAudio(downloadUrl)
                    }
                }
            }
        mExtractor.extract(link, true, true)
    }

    private fun playAudio(downloadUrl: String) {

        val p = _player.playerView.player
        ExoPlayerManager.getSharedInstance(context).playStream(downloadUrl)
        p.addListener(object : Player.EventListener {
            override fun onTimelineChanged(
                timeline: Timeline,
                manifest: Any?,
                reason: Int
            ) {

            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPlayerStateChanged(
                playWhenReady: Boolean,
                playbackState: Int
            ) {
                if (playbackState == Player.STATE_READY  && !isPlaying) {
                    val realDurationMillis = p.duration
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val ukrainianTime: Calendar =
                            GregorianCalendar(
                                TimeZone.getTimeZone("Europe/Kiev")
                            )
                        ukrainianTime.timeInMillis = Calendar.getInstance().timeInMillis
                        Log.d(
                            "currentTimeMillis",
                            ukrainianTime[Calendar.HOUR]
                                .toString() + " " + ukrainianTime[Calendar.MINUTE]
                        )
                        val startTime = 1610539200000L
                        val localTime = ukrainianTime.timeInMillis
                        val timePassed = localTime - startTime
                        val timesRepeat =
                            timePassed.toDouble() / realDurationMillis
                        Log.d(
                            "timesRepeated",
                            java.lang.Double.toString(timesRepeat % 1)
                        )
                        val trackSeek =
                            (timesRepeat % 1 * realDurationMillis).toLong()
                        p.seekTo(trackSeek)
                        isPlaying = true
                    }
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onPlayerError(error: ExoPlaybackException) {}
            override fun onPositionDiscontinuity(reason: Int) {}
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            override fun onSeekProcessed() {}
        })
    }
}