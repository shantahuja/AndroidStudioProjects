package com.example.zidakarainstagramapi.ui.content

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.SurfaceHolder
import android.widget.MediaController
import androidx.lifecycle.lifecycleScope
import com.example.zidakarainstagramapi.R
import kotlinx.android.synthetic.main.activity_video_player.*
import kotlinx.coroutines.launch

class VideoPlayerActivity : AppCompatActivity(), SurfaceHolder.Callback,
    MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        video_player.holder.addCallback(this)

       video_player.setOnClickListener {
           mediaController?.show()
       }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish();
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
    }

    var mediaPlayer: MediaPlayer? = null
    var mediaController: MediaController? = null
    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDisplay(surfaceHolder)
        try {
            mediaPlayer!!.setDataSource(intent.getStringExtra("video_url"))
            mediaPlayer!!.prepare()
            mediaPlayer!!.setOnPreparedListener(this)
            mediaController = MediaController(this)
        } catch(e: Exception){

        }
    }

    override fun onPrepared(p0: MediaPlayer?) {
        mediaPlayer?.start()
        mediaController?.setMediaPlayer(this)
        mediaController?.setAnchorView(video_player)
        lifecycleScope.launch {
            mediaController?.isEnabled = true
            mediaController?.show()
        }
    }

    override fun onDestroy() {
        mediaController?.hide()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        mediaController = null
        super.onDestroy()
    }

   // override fun onPause() {
 //       super.onPause()
  //  }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun canSeekForward(): Boolean {
       return true
    }

    override fun getDuration(): Int {
        return mediaPlayer?.duration ?:0
    }

    override fun pause() {
       mediaPlayer?.pause()
    }

    override fun getBufferPercentage(): Int {
        return 0
    }

    override fun seekTo(p0: Int) {
        mediaPlayer?.seekTo(p0)
    }

    override fun getCurrentPosition(): Int {
       return mediaPlayer?.currentPosition ?: 0
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun start() {
        mediaPlayer?.start()
    }

    override fun getAudioSessionId(): Int {
       return mediaPlayer!!.audioSessionId
    }

    override fun canPause(): Boolean {
        return true
    }
}