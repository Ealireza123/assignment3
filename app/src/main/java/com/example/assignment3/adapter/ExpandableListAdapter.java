package com.example.assignment3.adapter;

import static java.lang.String.valueOf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.assignment3.R;
import com.example.assignment3.model.LocationItem;
import com.example.assignment3.model.TimeBaseDataModel;
import com.google.android.material.divider.MaterialDivider;

import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final List<TimeBaseDataModel> data;


    public ExpandableListAdapter(Context context, List<TimeBaseDataModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View groupView =
                ((LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.group_item, parent, false);
        initGroupUI(groupView, isExpanded, data.get(groupPosition));

        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childView = ((LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.list_item, parent, false);
        initChildUI(childView, data.get(groupPosition).getLocationItemList().get(childPosition));

        return childView;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getLocationItemList().size();
    }

    @Override
    public TimeBaseDataModel getGroup(int groupPosition) {
        return data
                .get(groupPosition);
    }

    @Override
    public LocationItem getChild(int groupPosition, int childPosition) {
        return data
                .get(groupPosition)
                .getLocationItemList()
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void initChildUI(View childView, LocationItem locationItem) {
        TextView timeTextView = childView
                .findViewById(R.id.time_text_view);
        TextView angleTextView = childView
                .findViewById(R.id.angle_text_view);

        timeTextView.setText(valueOf(locationItem.getTime()));
        angleTextView.setText(valueOf(locationItem.getAngle()));
    }


    private void initGroupUI(View groupView, boolean isExpanded, TimeBaseDataModel item) {
        TextView recordedDateTextView = groupView.findViewById(R.id.record_data_text_view);
        MaterialDivider verticalDivider = groupView.findViewById(R.id.group_divider);
        LinearLayout tableHeadLayout = groupView.findViewById(R.id.table_head_layout);

        if (isExpanded) {
            verticalDivider.setVisibility(View.VISIBLE);
            tableHeadLayout.setVisibility(View.VISIBLE);
        } else {
            verticalDivider.setVisibility(View.GONE);
            tableHeadLayout.setVisibility(View.GONE);
        }
        recordedDateTextView.setText(item.getTestTime());

    }
}
