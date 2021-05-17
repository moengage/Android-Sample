package com.moengage.sample.kotlin

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.TextView
import java.util.*

class FeatureListAdapter(
    context: Context, expandableListTitle: List<String>,
    expandableListDetail: HashMap<String, List<String>>
) : ExpandableListAdapter {

    private val context: Context = context
    private val featureList: List<String> = expandableListTitle
    private val featureSubList: HashMap<String, List<String>> = expandableListDetail

    override fun registerDataSetObserver(observer: DataSetObserver?) {

    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {

    }

    override fun getGroupCount(): Int {
        return featureList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return featureSubList.get(featureList[groupPosition])?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any {
        return featureList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
        return featureSubList.get(featureList[groupPosition])?.get(childPosition) ?: "Error"
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?,
        parent: ViewGroup?
    ): View? {
        var view = convertView
        if (view == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.feature_list_item, parent, false)
        }
        if (view != null) {
            view.findViewById<TextView>(R.id.featureTitle).text = getGroup(groupPosition) as String
        }
        return view
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View? {
        var view = convertView
        if (view == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.feature_sub_list, parent, false)
        }
        if (view != null) {
            view.findViewById<TextView>(R.id.featureSubListTitle).text =
                getChild(groupPosition, childPosition)
        }
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun onGroupExpanded(groupPosition: Int) {

    }

    override fun onGroupCollapsed(groupPosition: Int) {

    }

    override fun getCombinedChildId(groupId: Long, childId: Long): Long {
        return 0
    }

    override fun getCombinedGroupId(groupId: Long): Long {
        return 0
    }

}
