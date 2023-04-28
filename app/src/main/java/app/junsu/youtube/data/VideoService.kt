package app.junsu.youtube.data

import app.junsu.youtube.model.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {

    @GET("/v3/2c9e6938-9510-473b-91d3-e123c6a2b314")
    fun getVideos(): Call<VideoDto>
}
