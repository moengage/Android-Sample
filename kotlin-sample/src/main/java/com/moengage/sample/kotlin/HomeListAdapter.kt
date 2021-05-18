package com.moengage.sample.kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moengage.sample.kotlin.databinding.FeatureListItemBinding

/**
 * @author Arshiya Khanum
 */
class HomeListAdapter(
    private val optionsList: List<HomeCategory>,
    private val categorySelected: OnHomeCategorySelected
) : RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.featureTitle.text = optionsList[position].label
        viewHolder.binding.root.setOnClickListener {
            categorySelected.onHomeCategorySelected(optionsList[position].category)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            FeatureListItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup, false
            )
        )
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    class ViewHolder(val binding: FeatureListItemBinding) : RecyclerView.ViewHolder(binding.root)
}

interface OnHomeCategorySelected {
    fun onHomeCategorySelected(category: FeaturesCategory)
}
