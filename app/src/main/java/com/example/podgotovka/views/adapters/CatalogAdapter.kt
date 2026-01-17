package com.example.podgotovka.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.podgotovka.R
import com.example.podgotovka.models.CatalogItem

class CatalogAdapter(
    private var catalogItems: List<CatalogItem> = emptyList(),
    private val onAddClickListener: (CatalogItem) -> Unit = {}
) : RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {

    inner class CatalogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextViewCatalog)
        private val subTitleTextView: TextView = itemView.findViewById(R.id.subTitleTextViewCatalog)
        private val saleTextView: TextView = itemView.findViewById(R.id.saleTextViewCatalog)
        private val btnAdd: Button = itemView.findViewById(R.id.btnAddCatalog)

        fun bind(item: CatalogItem) {
            titleTextView.text = item.title
            subTitleTextView.text = item.category
            saleTextView.text = item.price

            btnAdd.text = if (item.isAdded) "Добавлено" else "Добавить"
            btnAdd.isEnabled = !item.isAdded

            btnAdd.setOnClickListener {
                onAddClickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_catalog, parent, false)
        return CatalogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(catalogItems[position])
    }

    override fun getItemCount(): Int = catalogItems.size

    fun updateData(newItems: List<CatalogItem>) {
        catalogItems = newItems
        notifyDataSetChanged()
    }

    fun filterByCategory(category: String) {
        val filteredList = if (category == "Все") {
            getFullCatalog()
        } else {
            getFullCatalog().filter { it.category.contains(category, ignoreCase = true) }
        }
        updateData(filteredList)
    }

    // В реальном приложении здесь будет загрузка из базы данных или API
    fun getFullCatalog(): List<CatalogItem> {
        return listOf(
            CatalogItem(1, "Рубашка Воскресенье для машинного вязания", "Мужская одежда", "300 Р"),
            CatalogItem(2, "Платье Лето для ручного вязания", "Женская одежда", "450 Р"),
            CatalogItem(3, "Свитер Зима шерстяной", "Мужская одежда", "500 Р"),
            CatalogItem(4, "Юбка Весна кружевная", "Женская одежда", "350 Р"),
            CatalogItem(5, "Кардиган Осень пуловер", "Женская одежда", "400 Р"),
            CatalogItem(6, "Шарф Зима теплый", "Аксессуары", "200 Р"),
            CatalogItem(7, "Носки Домашние", "Мужская одежда", "150 Р"),
            CatalogItem(8, "Шапка Детская", "Детская одежда", "250 Р")
        )
    }
}