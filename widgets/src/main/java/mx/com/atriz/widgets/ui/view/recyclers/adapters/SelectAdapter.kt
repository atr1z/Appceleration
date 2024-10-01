package mx.com.atriz.widgets.ui.view.recyclers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.com.atriz.widgets.R
import mx.com.atriz.widgets.model.Option
import mx.com.atriz.widgets.ui.view.recyclers.viewholders.OptionViewHolder

class SelectAdapter(
    private val list: List<Option>,
    private val onSelectedOption: (Int, Option) -> Unit
) : RecyclerView.Adapter<OptionViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_option, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(list[position], selectedPos == position) { pos, option ->
            notifyItemChanged(selectedPos)
            notifyItemChanged(pos)
            selectedPos = pos
            onSelectedOption(position, option)
        }
    }
}
