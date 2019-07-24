package com.ceshui.mh.view

import android.content.Context
import android.media.MediaPlayer
import android.os.Environment
import android.widget.MediaController
import android.widget.VideoView
import java.io.File
import java.lang.NullPointerException

/**
 * Created by wangzhan on 2019-07-16 15:33
 */

class VideoViewWrapper {
    private lateinit var mediaController: MediaController
    private lateinit var videoView: VideoView
    private var repeat: Boolean = false
    private lateinit var context: Context

    constructor(context: Context) {
        this.context = context
    }

    fun setVideo(videoView: VideoView) {
        this.videoView = videoView
        this.mediaController = MediaController(context)
        this.videoView.setMediaController(this.mediaController)
        this.mediaController.setMediaPlayer(this.videoView)
    }

    /*
    * if is looping ,please @setPlayRepeat first
    * */
    fun setPlayPath(path: String) {
        checkVideo()
        this.videoView.setVideoPath(path)
        startPlay()
    }

    fun setPlayRepeat(repeat: Boolean) {
        this.repeat = repeat
    }

    private fun checkVideo() {
        if (videoView == null) {
            throw NullPointerException("VideoView has not init,please setVideo first!")
        }

    }

    private fun startPlay() {
        checkVideo()
        this.videoView.stopPlayback()
        this.videoView.seekTo(0)
        this.videoView.requestFocus()
        this.videoView.start()
        this.videoView.setOnCompletionListener {
            if (repeat) {
                it.start()
                it.isLooping = true
            }
        }
    }

    fun pausePlay() {
        checkVideo()
        this.videoView.pause()
    }

    fun stopPlay() {
        checkVideo()
        this.videoView.stopPlayback()
    }

    fun restartPlay() {
        checkVideo()
        startPlay()
    }
}