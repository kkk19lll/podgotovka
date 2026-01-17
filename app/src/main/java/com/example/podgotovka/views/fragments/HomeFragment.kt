package com.example.podgotovka.views.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.podgotovka.R
import com.example.podgotovka.databinding.FragmentHomeBinding
import com.example.podgotovka.models.ActionItem
import com.example.podgotovka.views.adapters.ActionsAdapter
import com.example.podgotovka.views.adapters.CatalogAdapter
import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var actionsAdapter: ActionsAdapter
    private lateinit var catalogAdapter: CatalogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionsRecyclerView()
        setupCatalogRecyclerView()
        setupChips()
        setupSearch()
    }

    private fun setupActionsRecyclerView() {
        val layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewActions.layoutManager = layoutManager

        val actionsList = listOf(
            ActionItem(
                title = "Шорты\nВторник",
                salePrice = "4000 Р",
                imageResId = R.drawable.image_med
            ),
            ActionItem(
                title = "Футболки\nСкидка 30%",
                salePrice = "2500 Р",
                imageResId = R.drawable.image_med
            )
        )

        actionsAdapter = ActionsAdapter(actionsList)
        binding.recyclerViewActions.adapter = actionsAdapter
    }

    private fun setupCatalogRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCatalog.layoutManager = layoutManager

        catalogAdapter = CatalogAdapter(onAddClickListener = { catalogItem ->
            // Обработка добавления товара
            // В реальном приложении здесь будет логика добавления в корзину или избранное
            showAddedMessage(catalogItem.title)
        })

        binding.recyclerViewCatalog.adapter = catalogAdapter

        // Загружаем начальные данные
        catalogAdapter.filterByCategory("Все")
    }

    private fun setupChips() {
        // Инициализируем начальное состояние чипов
        updateChipSelection(
            binding.chipAll, setOf(
                binding.chipAll,
                binding.chipWoman,
                binding.chipMan
            )
        )

        val chips = mapOf(
            binding.chipAll to "Все",
            binding.chipWoman to "Женщинам",
            binding.chipMan to "Мужчинам"
        )

        chips.forEach { (chip, category) ->
            chip.setOnClickListener {
                updateChipSelection(chip, chips.keys)
                catalogAdapter.filterByCategory(category)
            }
        }
    }

    private fun updateChipSelection(selectedChip: Chip, allChips: Set<Chip>) {
        allChips.forEach { chip ->
            val isSelected = chip == selectedChip

            // ВАЖНО: Используем chipBackgroundColor для Material Chip
            chip.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    if (isSelected) R.color.blue else R.color.gray
                )
            )

            // Устанавливаем цвет текста
            chip.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isSelected) R.color.white else R.color.text_gray
                )
            )

            // Устанавливаем обводку
            chip.chipStrokeColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    if (isSelected) R.color.blue else R.color.gray
                )
            )

            // Толщина обводки
            chip.chipStrokeWidth = if (isSelected) 0f else 2f

            // Обязательно обновляем состояние
            chip.invalidate()
            chip.requestLayout()
        }
    }

    private fun setupSearch() {
        binding.searchHome.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Можно добавить логику для отображения результатов поиска
            }
        }

        binding.searchHome.setOnEditorActionListener { _, _, _ ->
            // Обработка поиска при нажатии Enter
            performSearch(binding.searchHome.text.toString())
            true
        }
    }

    private fun performSearch(query: String) {
        // В реальном приложении здесь будет логика поиска по каталогу
        // Пока просто фильтруем по заголовку
        val filteredList = if (query.isBlank()) {
            catalogAdapter.getFullCatalog()
        } else {
            catalogAdapter.getFullCatalog().filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        }
        catalogAdapter.updateData(filteredList)
    }

    private fun showAddedMessage(itemTitle: String) {
        // Показываем уведомление об успешном добавлении
        // В реальном приложении можно использовать Snackbar или Toast
        // Snackbar.make(binding.root, "Добавлено: $itemTitle", Snackbar.LENGTH_SHORT).show()

        // Обновляем кнопку в адаптере
        // В реальном приложении нужно обновить конкретный элемент
        catalogAdapter.notifyDataSetChanged()
    }
}