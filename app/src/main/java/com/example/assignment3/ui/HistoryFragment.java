package com.example.assignment3.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.assignment3.R;
import com.example.assignment3.adapter.ExpandableListAdapter;
import com.example.assignment3.model.Repository;
import com.example.assignment3.model.TimeBaseDataModel;

import java.util.List;

public class HistoryFragment extends Fragment {
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ExpandableListView expandableListView = view.findViewById(R.id.expanded_list);
        LinearLayout linearLayout = view.findViewById(R.id.loading);

        List<TimeBaseDataModel> dbData = Repository.getInstance(requireContext()).getSearchedList();
        ExpandableListAdapter adapter = new ExpandableListAdapter(requireContext(), dbData);
        expandableListView.setAdapter(adapter);
        linearLayout.setVisibility(View.INVISIBLE);

        return view;
    }
}