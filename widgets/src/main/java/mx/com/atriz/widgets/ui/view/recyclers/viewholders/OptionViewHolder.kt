package mx.com.atriz.widgets.ui.view.recyclers.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import mx.com.atriz.widgets.R
import mx.com.atriz.widgets.databinding.ViewHolderOptionBinding
import mx.com.atriz.widgets.model.Option

class OptionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val binding = ViewHolderOptionBinding.bind(v)
    fun bind(
        option: Option,
        isSelected: Boolean,
        clickedOption: (Int, Option) -> Unit
    ) {
        binding.optionTitle.text = option.title
        option.subtitle?.let {
            binding.optionSubtitle.visibility = View.VISIBLE
            binding.optionSubtitle.text = it
        }
        binding.option.setOnClickListener {
            clickedOption(absoluteAdapterPosition, option)
        }

        if (isSelected) {
            binding.option.background = ContextCompat.getDrawable(
                binding.option.context,
                R.drawable.background_selected_option_rounded
            )
        } else {
            binding.option.background = null
        }
    }
}
