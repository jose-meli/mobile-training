package com.mercadolibre.jvillarnovo.trainingpractico1.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mercadolibre.jvillarnovo.trainingpractico1.Adapters.ListViewAdapter;
import com.mercadolibre.jvillarnovo.trainingpractico1.R;
import com.mercadolibre.jvillarnovo.trainingpractico1.ResultActivity;
import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;

import java.util.LinkedList;

/**
 * Created by jvillarnovo on 18/11/14.
 */
public class ListViewFragment extends ListFragment {

    public interface IOnItemClick {
        public void onItemClick(Item item);
    }

    public static final String RESULT_LIST = "Result_list";
    private ListView listViewResults;
    private ListViewAdapter listViewAdapter;
    private String itemSearch;
    private IOnItemClick callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (IOnItemClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getComponentName() + " must implement IOnItemClick");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        initComponents(inflater, view, savedInstanceState);
        return view;
    }

    private void initComponents(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        itemSearch = getActivity().getIntent().getStringExtra(ResultActivity.ITEM_SEARCH);
        listViewResults = (ListView) view.findViewById(android.R.id.list);
        if (savedInstanceState == null) {
            if (getArguments() == null) {
                listViewAdapter = new ListViewAdapter(inflater, new LinkedList<Item>(), itemSearch);
                listViewAdapter.sendRequestItems(16, 0);
            } else {
                listViewAdapter = new ListViewAdapter(inflater,
                        (LinkedList<Item>) getArguments().getSerializable(RESULT_LIST), itemSearch);
            }
        } else {
            listViewAdapter = new ListViewAdapter(inflater,
                    (LinkedList<Item>) savedInstanceState.getSerializable(RESULT_LIST), itemSearch);
        }
        listViewResults.setAdapter(listViewAdapter);
        listViewResults.setOnScrollListener(listViewAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RESULT_LIST, listViewAdapter.getListItems());
    }

    public LinkedList<Item> getItemsList() {
        return listViewAdapter.getListItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                callback.onItemClick((Item) adapterView.getItemAtPosition(position));
            }
        });
        listViewAdapter.notifyDataSetChanged();
    }
}
