package app.junsu.youtube.model

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String,
    @SerializedName("description") val description: String,
    @SerializedName("thumb") val thumb: String,
    @SerializedName("sources") val sources: String,
)
