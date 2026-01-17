package com.example.podgotovka.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.podgotovka.R
import com.example.podgotovka.databinding.ItemActionBinding
import com.example.podgotovka.models.ActionItem

class ActionsAdapter(private val actionsList: List<ActionItem>) :
    RecyclerView.Adapter<ActionsAdapter.ActionViewHolder>() {

    // Список градиентов/фонов для разных позиций
    private val backgroundGradients = listOf(
        R.drawable.gradient_background_1,
        R.drawable.gradient_background_2,
    )

    inner class ActionViewHolder(private val binding: ItemActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(actionItem: ActionItem, position: Int) {
            binding.titleTextView.text = actionItem.title
            binding.saleTextView.text = actionItem.salePrice
            binding.imageViewAction.setImageResource(actionItem.imageResId)

            // Устанавливаем фон в зависимости от позиции
            val backgroundIndex = position % backgroundGradients.size
            binding.rlBackgroundAction.setBackgroundResource(backgroundGradients[backgroundIndex])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemActionBinding.inflate(inflater, parent, false)
        return ActionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        holder.bind(actionsList[position], position)
    }

    override fun getItemCount(): Int = actionsList.size
}