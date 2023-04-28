package app.junsu.youtube.model

import com.google.gson.annotations.SerializedName

data class VideoDto(
    @SerializedName("videos") val videos: List<Video>,
)
