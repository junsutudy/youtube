package app.junsu.youtube.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.junsu.youtube.R
import app.junsu.youtube.model.Video
import com.bumptech.glide.Glide

class VideoAdapter : ListAdapter<Video, VideoAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(
        private val view: View,
    ) : RecyclerView.ViewHolder(view) {
        fun bind(
            item: Video,
        ) {
            val imgThumbnail = view.findViewById<ImageView>(R.id.img_video_thumbnail)
            val tvTitle = view.findViewById<TextView>(R.id.tv_video_title)
            val tvSubTitle = view.findViewById<TextView>(R.id.tv_video_subtitle)

            Glide.with(
                imgThumbnail.context,
            ).load(
                item.thumb,
            ).into(
                imgThumbnail,
            )

            tvTitle.text = item.title
            tvSubTitle.text = item.subtitle
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder = ViewHolder(
        view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_video, parent, false
        ),
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        return holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(
                oldItem: Video,
                newItem: Video,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Video,
                newItem: Video,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
