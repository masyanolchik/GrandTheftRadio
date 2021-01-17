package com.example.anothertryofmusicanim.playerview

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.anothertryofmusicanim.R
import timber.log.Timber

class PlayerView @JvmOverloads constructor(context: Context,
                             attrs: AttributeSet? =null,
                             defStyleAttr: Int = 0) : MotionLayout(context,attrs,defStyleAttr) {
    private var isPlaying: Boolean = false
    private var motionLayout: MotionLayout
    private val touchableArea: View
    private lateinit var playerClick:PlayerViewOnClickListener
    private var trackName: TextView
    private var trackArtist:TextView
    private var trackImage:ImageView


    private val clickableArea: ImageView

    private var startX: Float? = null
    private var startY: Float? = null

    lateinit var playButton: ImageButton

    init {
        motionLayout = LayoutInflater.from(context).inflate(R.layout.mini_player, this, false) as MotionLayout
        addView(motionLayout)
        playButton = motionLayout.findViewById(R.id.play_button)
        trackName = motionLayout.findViewById(R.id.track_name)
        trackArtist = motionLayout.findViewById(R.id.artist_name)
        trackImage = motionLayout.findViewById(R.id.track_album)
        val prevButton: ImageButton = motionLayout.findViewById(R.id.prev_button)
        val nextButton: ImageButton = motionLayout.findViewById(R.id.next_button)
        playButton.setOnClickListener {
            if(isPlaying){
                playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                isPlaying = false
            } else {
                playButton.setImageResource(R.drawable.ic_baseline_pause_24)
                isPlaying = true
            }
            try{
                playerClick.playButtonHandler()
            } catch (e: UninitializedPropertyAccessException){
                Timber.e(e)
            }
        }
        prevButton.setOnClickListener {
            try{
                playerClick.prevButtonHandler()
            } catch (e: UninitializedPropertyAccessException){
                Timber.e(e)
            }
        }
        nextButton.setOnClickListener {
            try{
                playerClick.nextButtonHandler()
            } catch (e: UninitializedPropertyAccessException){
                Timber.e(e)
            }
        }
        //trackImage.setImageResource(R.drawable.ic_brimage)
        touchableArea = motionLayout.findViewById(R.id.touchPanel)
        clickableArea = motionLayout.findViewById(R.id.track_album)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val isInProgress = (motionLayout.progress > 0.0f && motionLayout.progress < 1.0f)
        val isInTarget = touchEventInsideTargetView(touchableArea, ev)

        return if (isInProgress || isInTarget) {
            super.onInterceptTouchEvent(ev)
        } else {
            true
        }
    }

    fun invokePlayButton(){
        playButton.performClick()
    }

    fun setOnViewListener(listener: PlayerViewOnClickListener){
        this.playerClick = listener
    }

    fun setAlbumImage(btmp:Bitmap){
        trackImage.setImageBitmap(btmp)
    }

    fun setArtistName(artist: String){
        trackArtist.text = artist
    }

    fun setTrackName(track: String){
        trackName.text = track
    }

    private fun touchEventInsideTargetView(v: View, ev: MotionEvent): Boolean {
        if (ev.x > v.left && ev.x < v.right) {
            if (ev.y > v.top && ev.y < v.bottom) {
                return true
            }
        }
        return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (touchEventInsideTargetView(clickableArea, ev)) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = ev.x
                    startY = ev.y
                }

                MotionEvent.ACTION_UP   -> {
                    val endX = ev.x
                    val endY = ev.y
                    if (startX!=null && startY!=null){
                        if (isAClick(startX!!, endX, startY!!, endY)) {
                            if (doClickTransition()) {
                                return true
                            }
                        }
                    }
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun doClickTransition(): Boolean {
        var isClickHandled = false
        if (motionLayout.progress < 0.05F) {
            motionLayout.transitionToEnd()
            isClickHandled = true
        } else if (motionLayout.progress > 0.95F) {
            motionLayout.transitionToStart()
            isClickHandled = true
        }
        return isClickHandled
    }

    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val differenceX = Math.abs(startX - endX)
        val differenceY = Math.abs(startY - endY)
        return !/* =5 */(differenceX > 200 || differenceY > 200)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }
}