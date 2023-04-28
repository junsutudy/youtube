package app.junsu.youtube.data

import app.junsu.youtube.model.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {

    @GET("/v3/f99f7386-55fc-4ea9-b087-ac0f3e30a68b")
    fun getVideos(): Call<VideoDto>
}
