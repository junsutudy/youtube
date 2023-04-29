package app.junsu.youtube.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import app.junsu.youtube.R
import app.junsu.youtube.data.VideoService
import app.junsu.youtube.databinding.FragmentPlayerBinding
import app.junsu.youtube.model.VideoDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment : Fragment(R.layout.fragment_player) {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var videoAdapter: VideoAdapter

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

    fun play(
        url: String,
        title: String,
    ) {
        binding.motionLayoutPlayer.transitionToEnd()
        binding.tvPlayerTitle.text = title
    }
}
