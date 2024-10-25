package com.volkov.listvacancy.presentation.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volkov.core.utils.WordDeclension
import com.volkov.listvacancy.R
import com.volkov.listvacancy.databinding.ItemShowMoreButtonBinding
import com.volkov.listvacancy.databinding.VacancyItemBinding
import com.volkov.listvacancy.presentation.model.ListVacancyModel

class ListVacancyAdapter(
    private var vacancies: List<ListVacancyModel>,
    private var onVacancyClick: (ListVacancyModel) -> Unit,
    private var onFavoriteClick: (ListVacancyModel) -> Unit,
    private var onShowMoreClick: () -> Unit, // Кнопка "Еще вакансий"
    private var onApplyClick: (ListVacancyModel) -> Unit // Кнопка "Еще вакансий"
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_TYPE_VACANCY = 0
    private val ITEM_TYPE_SHOW_MORE = 1

    // состояние списка вакансий
    private var isFullListDisplayed = false

    // общий список вакансий
    private var totalVacanciesCount: Int = vacancies.size

    // ViewHolder для вакансий
    class VacancyViewHolder(val binding: VacancyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val wordDeclension = WordDeclension()

        fun bind(
            vacancy: ListVacancyModel,
            onVacancyClick: (ListVacancyModel) -> Unit,
            onFavoriteClick: (ListVacancyModel) -> Unit,
            onApplyClick: (ListVacancyModel) -> Unit // Кнопка "Еще вакансий"
        ) {
            if (vacancy.lookingNumber > 0) {
                val human = wordDeclension.getHumanCountString(
                    vacancy.lookingNumber.toInt(),
                    itemView.context
                )
                // добавляю через itemView так как нужен контекст
                // склонение текста в зависимости от числа lookingNumber
                binding.itemPeoplCount.text =
                    itemView.context.getString(R.string.now_browsing, human)
                binding.itemPeoplCount.visibility = View.VISIBLE
            } else {
                binding.itemPeoplCount.visibility = View.GONE
            }
            binding.itemVacancyTitle.text = vacancy.title
            binding.itemVacancySalary.text = vacancy.salary.full
            binding.itemVacancyAdress.text = vacancy.address.town
            binding.itemVacancyCompany.text = vacancy.company
            binding.itemExperience.text = vacancy.experience.previewText
            binding.itemDate.text =
                itemView.context.getString(R.string.published, vacancy.publishedDate)

            binding.searchLikeBttn.setImageResource(
                if (vacancy.isFavorite) R.drawable.fill_heart_icon else R.drawable.heart_icon
            )
            // Обработка нажатия на весь элемент
            itemView.setOnClickListener { onVacancyClick(vacancy) }
            binding.searchLikeBttn.setOnClickListener { onFavoriteClick(vacancy) }
            binding.responsButton.setOnClickListener { onApplyClick(vacancy) } // Обработка нажатия на кнопку "Откликнуться"
        }

    }

    // ViewHolder для кнопки "Еще вакансий"
    class ShowMoreViewHolder(val binding: ItemShowMoreButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // private val showMoreButton: Button = itemView.findViewById(R.id.more_vacancies_button)
        private val wordDeclension = WordDeclension()
        fun bind(onShowMoreClick: () -> Unit, remainingCount: Int) {
            // склонение слова Вакансия, в зависимости от количества вакансий в списке
            val vacancy = wordDeclension.getVacancyCountString(remainingCount, itemView.context)
            binding.moreVacanciesButton.text = itemView.context.getString(R.string.more, vacancy)
            binding.moreVacanciesButton.setOnClickListener { onShowMoreClick() }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 3 || isFullListDisplayed) ITEM_TYPE_VACANCY else ITEM_TYPE_SHOW_MORE
    }

    fun updateVacancies(
        allvacancies: List<ListVacancyModel>,
        showFullList: Boolean,
        totalVacanciesCount: Int
    ) {
        isFullListDisplayed = showFullList
        this.totalVacanciesCount = totalVacanciesCount
        this.vacancies = allvacancies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_VACANCY) {
            val binding: VacancyItemBinding =
                VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            VacancyViewHolder(binding)
        } else {
            val binding: ItemShowMoreButtonBinding = ItemShowMoreButtonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ShowMoreViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        // Возвращаю размер списка + 1, чтобы показать кнопку "Еще вакансий"
        return if (isFullListDisplayed) vacancies.size else 4
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_TYPE_VACANCY) {
            val vacancy = vacancies[position]
            (holder as VacancyViewHolder).bind(
                vacancy,
                onVacancyClick,
                onFavoriteClick,
                onApplyClick
            )
        } else {
            val remainingVacancies = totalVacanciesCount - 3
            (holder as ShowMoreViewHolder).bind(onShowMoreClick, remainingVacancies)
        }
    }
}