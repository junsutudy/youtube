package app.junsu.youtube.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import app.junsu.youtube.R
import app.junsu.youtube.data.VideoService
import app.junsu.youtube.model.VideoDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.container_main,
            PlayerFragment(),
        ).commit()

        getVideos()
    }

    private fun getVideos() {
        println("HELLOOO")
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
                        Log.d("RESPONSE", response.body().toString())
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