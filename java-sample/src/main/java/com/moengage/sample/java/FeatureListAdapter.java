package com.moengage.sample.java;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.List;


public class FeatureListAdapter implements ExpandableListAdapter {

  private Context context;
  private List<String> featureList;
  private HashMap<String, List<String>> featureSubList;

  public FeatureListAdapter(@NonNull Context context, @NonNull List<String> expandableListTitle,
      @NonNull HashMap<String, List<String>> expandableListDetail) {
    this.context = context;
    this.featureList = expandableListTitle;
    this.featureSubList = expandableListDetail;
  }

  @Override public void registerDataSetObserver(DataSetObserver observer) {

  }

  @Override public void unregisterDataSetObserver(DataSetObserver observer) {

  }

  @Override public int getGroupCount() {
    return featureList.size();
  }

  @Override public int getChildrenCount(int groupPosition) {
    return featureSubList.get(featureList.get(groupPosition)).size();
  }

  @Override public Object getGroup(int groupPosition) {
    return featureList.get(groupPosition);
  }

  @Override public String getChild(int groupPosition, int childPosition) {
    return featureSubList.get(featureList.get(groupPosition)).get(childPosition);
  }

  @Override public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override public boolean hasStableIds() {
    return false;
  }

  @Override public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
      ViewGroup parent) {
    if (convertView == null) {
      LayoutInflater layoutInflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = layoutInflater.inflate(R.layout.feature_list_item, parent, false);
    }

    TextView featureTitle = convertView.findViewById(R.id.featureTitle);

    featureTitle.setText((String) getGroup(groupPosition));

    return convertView;
  }

  @Override public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
      View convertView, ViewGroup parent) {
    if (convertView == null) {
      LayoutInflater layoutInflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = layoutInflater.inflate(R.layout.feature_sub_list, parent, false);
    }

    TextView featureSubTitle = (TextView) convertView.findViewById(R.id.featureSubListTitle);

    String subItem = getChild(groupPosition, childPosition);
    featureSubTitle.setText(subItem);

    return convertView;
  }

  @Override public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  @Override public boolean areAllItemsEnabled() {
    return false;
  }

  @Override public boolean isEmpty() {
    return false;
  }

  @Override public void onGroupExpanded(int groupPosition) {

  }

  @Override public void onGroupCollapsed(int groupPosition) {

  }

  @Override public long getCombinedChildId(long groupId, long childId) {
    return 0;
  }

  @Override public long getCombinedGroupId(long groupId) {
    return 0;
  }
}
