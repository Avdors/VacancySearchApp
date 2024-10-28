package com.volkov.listvacancy.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.volkov.listvacancy.R
import com.volkov.listvacancy.databinding.OfferItemBinding
import com.volkov.listvacancy.presentation.model.ListOfferModel

class ListVacOfferAdapter(
    private var offers: List<ListOfferModel>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ListVacOfferAdapter.OfferViewHolder>() {
    class OfferViewHolder(val binding: OfferItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(offer: ListOfferModel, onItemClick: (String) -> Unit) {
            // Устанавливаю иконки в зависимости от id
            val iconResId = when (offer.id) {
                "near_vacancies" -> R.drawable.near_map_icon
                "level_up_resume" -> R.drawable.level_up_resume_icon
                "temporary_job" -> R.drawable.temporary_job_icon
                else -> null
            }
            if (iconResId != null) {
                binding.recommendationIcon.setImageResource(iconResId)
                binding.recommendationIcon.visibility = View.VISIBLE
            } else {
                binding.recommendationIcon.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onItemClick(offer.link)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {

        val binding: OfferItemBinding =
            OfferItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun getItemCount(): Int = offers.size

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(offers[position], onItemClick)
    }

    fun updateOffers(newOffers: List<ListOfferModel>) {
        this.offers = newOffers
        notifyDataSetChanged()
    }
}