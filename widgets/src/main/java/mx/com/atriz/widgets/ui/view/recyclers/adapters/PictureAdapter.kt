package mx.com.atriz.widgets.ui.view.recyclers.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.com.atriz.core.entities.Actions
import mx.com.atriz.widgets.R
import mx.com.atriz.widgets.ui.view.recyclers.viewholders.PictureViewHolder
import mx.com.atriz.widgets.ui.view.recyclers.viewholders.TakePictureViewHolder

class PictureAdapter(
    private val takePictureAction: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int =
        if (position == 0) Actions.TAKE_PICTURE.ordinal else Actions.PICTURE.ordinal

    private var items: MutableList<Bitmap?> = mutableListOf(null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (Actions.get(viewType)) {
            Actions.TAKE_PICTURE -> TakePictureViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_add_pic,
                    parent,
                    false
                )
            )

            Actions.PICTURE -> PictureViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_picture_card,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TakePictureViewHolder -> holder.itemView.setOnClickListener {
                takePictureAction()
            }

            is PictureViewHolder -> {
                items[position]?.let { picture ->
                    holder.setContent(picture)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPictures(pictures: List<Bitmap>) {
        this.items.clear()
        this.items.add(null)
        this.items.addAll(pictures)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPicture(picture: Bitmap) {
        this.items.add(picture)
        notifyDataSetChanged()
    }
}
