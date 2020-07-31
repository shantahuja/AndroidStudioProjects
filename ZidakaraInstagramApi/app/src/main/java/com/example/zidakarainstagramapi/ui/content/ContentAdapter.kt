package com.example.zidakarainstagramapi.ui.content

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zidakarainstagramapi.R
import com.example.zidakarainstagramapi.data.Content
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.content_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class ContentAdapter(val coroutineScope: LifecycleCoroutineScope, val clickHandler: ((String) -> Unit)? = null) : ListAdapter<Content, ContentAdapter.ContentViewHolder>(ContentDiffCallback()) {


    var selectionTracker: SelectionTracker<Long>? = null

    inner class ContentViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(content: Content) = with(itemView) {
            if (selectionTracker != null) bindSelectedState(this, selectionTracker!!.isSelected(content.id))
            else if (clickHandler != null) setOnClickListener {
                clickHandler!!(content.media_url)
            }
            content_item_caption.text = content.caption
            val url = content.media_url

            coroutineScope.launchWhenStarted {
                val image = retrieveVideoFrameFromVideo(url)
                content_item_thumbnail.setImageBitmap(image)
            }
        }

        fun bindSelectedState(view: View, selected: Boolean) {
            view.isActivated = selected
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? =
                    this@ContentAdapter.currentList[adapterPosition].id
            }
    }

    class DetailsLookup(val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(e.x, e.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as ContentViewHolder).getItemDetails()
            }
            return null
        }
    }

    class KeyProvider(private val adapter: ContentAdapter) : ItemKeyProvider<Long>(SCOPE_CACHED) {
        override fun getKey(position: Int): Long? = adapter.currentList[position].id
        override fun getPosition(key: Long): Int =
            adapter.currentList.indexOfFirst { it.id == key }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_item, parent, false)
        return ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

fun retrieveVideoFrameFromVideo(videoPath: String): Bitmap {
    var mediaMetadataRetriever: MediaMetadataRetriever? = null
    var bitmap : Bitmap? = null
    try {
        mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(videoPath, HashMap())
        bitmap = mediaMetadataRetriever.frameAtTime
    } catch (e: Exception) {
        e.printStackTrace()
        throw Exception("Unable to retrieve image" + e.message)
    } finally {
        mediaMetadataRetriever?.release()
    }
    if (bitmap == null) {
        throw Exception("Bitmap is null")
    }
    return bitmap
}

class ContentDiffCallback : DiffUtil.ItemCallback<Content>() {
    override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem == newItem
    }

}