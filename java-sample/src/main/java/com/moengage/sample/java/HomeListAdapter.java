package com.moengage.sample.java;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.moengage.sample.java.databinding.FeatureListItemBinding;
import java.util.List;

/**
 * @author Arshiya Khanum
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

  private final List<HomeCategory> optionsList;
  private final OnHomeCategorySelected onHomeCategorySelected;

  public HomeListAdapter(List<HomeCategory> optionsList, OnHomeCategorySelected onHomeCategorySelected) {
    this.optionsList = optionsList;
    this.onHomeCategorySelected = onHomeCategorySelected;
  }

  @NonNull @Override
  public HomeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(
        FeatureListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
        false));
  }

  @Override public void onBindViewHolder(@NonNull HomeListAdapter.ViewHolder holder, int position) {
    holder.binding.featureTitle.setText(optionsList.get(position).getLabel());
    holder.binding.getRoot().setOnClickListener(
        v -> onHomeCategorySelected.onHomeCategorySelected(optionsList.get(position).getCategory()));
  }

  @Override public int getItemCount() {
    return optionsList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public FeatureListItemBinding binding;

    public ViewHolder(@NonNull FeatureListItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  interface OnHomeCategorySelected {

    public void onHomeCategorySelected(FeaturesCategory category);

  }
}
