package com.habitrpg.wearos.habitica.ui.viewHolders

import android.view.View
import androidx.core.content.ContextCompat
import com.habitrpg.android.habitica.R
import com.habitrpg.android.habitica.databinding.RowHeaderBinding
import com.habitrpg.android.habitica.databinding.RowSectionHeaderBinding

class HeaderViewHolder(itemView: View): BindableViewHolder<String>(itemView) {
    private val binding = RowHeaderBinding.bind(itemView)

    override fun bind(data: String) {
        if (data == itemView.context.resources.getString(R.string.settings)) {
            binding.textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
        }
        binding.textView.text = data
    }
}

class HeaderSectionViewHolder(itemView: View): BindableViewHolder<String>(itemView) {
    private val binding = RowSectionHeaderBinding.bind(itemView)

    override fun bind(data: String) {
        binding.textView.text = data
    }
}