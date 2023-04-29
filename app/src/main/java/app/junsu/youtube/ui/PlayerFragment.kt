package app.junsu.youtube.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import app.junsu.youtube.R
import app.junsu.youtube.data.VideoService
import app.junsu.youtube.databinding.FragmentPlayerBinding
import app.junsu.youtube.model.VideoDto
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment : Fragment(R.layout.fragment_player) {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var player: ExoPlayer

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerBinding.bind(view)

        videoAdapter = VideoAdapter(
            callback = this::play,
        )

        initMotionLayoutEvent()
        initRecyclerView()
        getVideos()

        initPlayer()
    }

    private fun initMotionLayoutEvent() {
        binding.motionLayoutPlayer.setTransitionListener(
            object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                ) {
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float,
                ) {
                    binding.let {
                        (activity as MainActivity).also { mainActivity ->
                            mainActivity.findViewById<MotionLayout>(
                                R.id.motion_layout_main,
                            ).progress = abs(progress)
                        }
                    }
                }

                override fun onTransitionCompleted(
                    motionLayout: MotionLayout?,
                    currentId: Int,
                ) {
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float,
                ) {
                }
            },
        )
    }

    private fun getVideos() {
        val retrofit = Retrofit.Builder().baseUrl(
            "https://run.mocky.io",
        ).addConverterFactory(
            GsonConverterFactory.create(),
        ).build()

        retrofit.create(VideoService::class.java).also {
            it.getVideos().enqueue(
                object : Callback<VideoDto> {
                    override fun onResponse(
                        call: Call<VideoDto>,
                        response: Response<VideoDto>,
                    ) {
                        if (response.isSuccessful.not()) {
                            Log.e("CALL", "FAILED")
                            return
                        }

                        response.body()?.let { videoDto ->
                            videoAdapter.submitList(videoDto.videos)
                        }
                    }

                    override fun onFailure(
                        call: Call<VideoDto>,
                        t: Throwable,
                    ) {
                        t.printStackTrace()
                    }
                },
            )
        }
    }

    private fun initRecyclerView() {
        binding.rvPlayer.run {
            adapter = videoAdapter
        }
    }

    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.let {
            player.addListener(
                object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        it.btnPlayerController.setImageResource(
                            if (isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24,
                        )
                    }
                },
            )
        }

        binding.playerViewPlayer.player = player
    }

    fun play(
        url: String,
        title: String,
    ) {
        val dataSourceFactory = DefaultDataSourceFactory(requireContext())
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.fromUri(
                Uri.parse(url),
            ),
        )

        player.run {
            setMediaSource(mediaSource)
            prepare()
            play()
        }

        binding.motionLayoutPlayer.transitionToEnd()
        binding.tvPlayerTitle.text = title
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
