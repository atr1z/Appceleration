package mx.com.atriz.widgets.ui.view.recyclers.viewholders

import android.graphics.Bitmap
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import mx.com.atriz.widgets.databinding.ItemPictureCardBinding
import mx.com.atriz.widgets.utils.DecodeBitmapTask

@Suppress("DEPRECATION")
class PictureViewHolder(view: View) : RecyclerView.ViewHolder(view), DecodeBitmapTask.Listener {

    private val binding = ItemPictureCardBinding.bind(view)

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    private var task: DecodeBitmapTask? = null

    fun setContent(@DrawableRes resId: Int) {
        if (viewWidth == 0) {
            itemView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    viewWidth = itemView.width
                    viewHeight = itemView.height
                    loadBitmap(resId)
                }
            })
        } else {
            loadBitmap(resId)
        }
    }

    fun setContent(bitmap: Bitmap) {
        binding.image.setImageBitmap(bitmap)
    }

    fun clearContent() {
        task?.cancel(true)
    }

    private fun loadBitmap(@DrawableRes resId: Int) {
        task = DecodeBitmapTask(itemView.resources, resId, viewWidth, viewHeight, this)
        task?.execute()
    }

    override fun onPostExecuted(bitmap: Bitmap) {
        binding.image.setImageBitmap(bitmap)
    }
}
