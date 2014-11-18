package com.mercadolibre.jvillarnovo.trainingpractico1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mercadolibre.jvillarnovo.trainingpractico1.Adapters.ListViewAdapter;
import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;

import java.io.Serializable;
import java.util.LinkedList;


public class ResultActivity extends Activity {

    public static final String ITEM_SEARCH = "item_search";
    private ListView listViewResults;
    private ListViewAdapter listViewAdapter;
    private String itemSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initComponents(savedInstanceState);
    }

    public void initComponents(Bundle savedInstanceState) {
        itemSearch = getIntent().getStringExtra(ITEM_SEARCH);
        listViewResults = (ListView) findViewById(R.id.listResults);
        if (savedInstanceState == null) {
            listViewAdapter = new ListViewAdapter(getLayoutInflater(), new LinkedList<Item>(), itemSearch);
            listViewAdapter.sendRequestItems(16, 0);
        } else {
            listViewAdapter = new ListViewAdapter(getLayoutInflater(),
                    (LinkedList<Item>) savedInstanceState.getSerializable("listResults"), itemSearch);
        }
        listViewResults.setAdapter(listViewAdapter);
        listViewResults.setOnScrollListener(listViewAdapter);
        listViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);
                intent.putExtra(ItemDetailActivity.ITEM, (Serializable) adapterView.getItemAtPosition(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listResults", listViewAdapter.getListItems());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
