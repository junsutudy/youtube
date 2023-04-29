package app.junsu.youtube.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.RecyclerView
import app.junsu.youtube.R
import app.junsu.youtube.data.VideoService
import app.junsu.youtube.model.VideoDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            PlayerFragment(),
        ).commit()

        videoAdapter = VideoAdapter { url, title ->
            supportFragmentManager.fragments.find { it is PlayerFragment }?.let {
                (it as PlayerFragment).play(
                    url = url,
                    title = title
                )
            }
        }

        findViewById<RecyclerView>(R.id.rv_main).apply {
            adapter = videoAdapter
        }

        getVideos()
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
}