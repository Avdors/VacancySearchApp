package com.volkov.favoritevacancy.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volkov.core.utils.WordDeclension
import com.volkov.favoritevacancy.R
import com.volkov.favoritevacancy.databinding.VacancyItemBinding
import com.volkov.favoritevacancy.presentation.model.FavoriteVacancyModel

class FavoriteVacancyAdapter(
    private var vacancies: List<FavoriteVacancyModel>,
    private val onVacancyClick: (FavoriteVacancyModel) -> Unit,
    private val onFavoriteClick: (FavoriteVacancyModel) -> Unit,
    private val onApplyClick: (FavoriteVacancyModel) -> Unit // Кнопка "Откликнуться"
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // общий список вакансий
    private var totalVacanciesCount: Int = vacancies.size

    class VacancyViewHolder(val binding: VacancyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val wordDeclension = WordDeclension()

        fun bind(
            vacancy: FavoriteVacancyModel,
            onVacancyClick: (FavoriteVacancyModel) -> Unit,
            onFavoriteClick: (FavoriteVacancyModel) -> Unit,
            onApplyClick: (FavoriteVacancyModel) -> Unit
        ) {
            if (vacancy.lookingNumber > 0) {
                val human = wordDeclension.getHumanCountString(
                    vacancy.lookingNumber.toInt(),
                    itemView.context
                )
                binding.itemPeoplCount.text = "Сейчас просматривают $human"
                binding.itemPeoplCount.visibility = View.VISIBLE
            } else {
                binding.itemPeoplCount.visibility = View.GONE
            }

            binding.itemVacancyTitle.text = vacancy.title
            binding.itemVacancySalary.text = vacancy.salary.full
            binding.itemVacancyAdress.text = vacancy.address.town
            binding.itemVacancyCompany.text = vacancy.company
            binding.itemExperience.text = vacancy.experience.previewText
            binding.itemDate.text = "Опубликовано ${vacancy.publishedDate}"


            binding.searchLikeBttn.setImageResource(
                if (vacancy.isFavorite) R.drawable.fill_heart_icon else R.drawable.heart_icon
            )
            // Обработка нажатия на весь элемент
            itemView.setOnClickListener { onVacancyClick(vacancy) }
            binding.searchLikeBttn.setOnClickListener { onFavoriteClick(vacancy) }
            binding.responsButton.setOnClickListener { onApplyClick(vacancy) } // Обработка нажатия на кнопку "Откликнуться"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: VacancyItemBinding =
            VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return vacancies.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vacancy = vacancies[position]
        Log.d("VacancyAdapter", "Binding vacancy at position: $position")
        (holder as VacancyViewHolder).bind(
            vacancy,
            onVacancyClick,
            onFavoriteClick,
            onApplyClick
        )
    }

    fun updateVacancies(
        allVacancies: List<FavoriteVacancyModel>,
        totalVacanciesCount: Int
    ) {
        Log.d("ListVacancyAdapter", "Updating vacancies: ${allVacancies.size}")
        this.totalVacanciesCount = totalVacanciesCount
        this.vacancies = allVacancies
        notifyDataSetChanged()
    }
}