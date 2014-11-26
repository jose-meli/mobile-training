package com.mercadolibre.jvillarnovo.trainingpractico1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;
import com.mercadolibre.jvillarnovo.trainingpractico1.fragment.ListViewFragment;


public class ResultActivity extends Activity implements ListViewFragment.IOnItemClick {

    public static final String ITEM_SEARCH = "item_search";
    private static final String FRAGMENT_LIST = "Fragment_List";
    private static final String FRAGMENT_ITEM_VIP_VIEW = "Fragment_Item_Vip_View";
    private ListViewFragment listView;
    private ItemDetailActivity itemVipView;
    private Item itemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initComponents(savedInstanceState);
    }

    public void initComponents(Bundle savedInstanceState) {
        listView = new ListViewFragment();
        itemVipView = new ItemDetailActivity();
        if (savedInstanceState != null) {
            itemSelected = (Item) savedInstanceState.getSerializable(ItemDetailActivity.ITEM);
            listView.setArguments(savedInstanceState);
            itemVipView.setArguments(savedInstanceState);
        }
        loadListView();
        loadVipView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ItemDetailActivity.ITEM, itemSelected);
        outState.putSerializable(ListViewFragment.RESULT_LIST, listView.getItemsList());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public void onItemClick(Item item) {
        itemSelected = item;
        Bundle b = new Bundle();
        b.putSerializable(ItemDetailActivity.ITEM, itemSelected);
        itemVipView = new ItemDetailActivity();
        itemVipView.setArguments(b);
        loadVipView();
    }

    private void loadListView() {
        if (findViewById(R.id.fragmentList) == null) {
            getFragmentManager().beginTransaction().replace(R.id.result, listView, FRAGMENT_LIST).commit();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.fragmentList, listView, FRAGMENT_LIST).commit();
        }
    }

    private void loadVipView() {
        if (itemSelected != null) {
            if (findViewById(R.id.fragmentVipView) == null) {
                getFragmentManager().beginTransaction().replace(R.id.result, itemVipView, FRAGMENT_ITEM_VIP_VIEW).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragmentVipView, itemVipView, FRAGMENT_ITEM_VIP_VIEW).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.scrollViewItemDetail) != null) {
            itemSelected = null;
            Bundle b = new Bundle();
            b.putSerializable(ListViewFragment.RESULT_LIST, listView.getItemsList());
            listView.setArguments(b);
            loadListView();
        } else {
            super.onBackPressed();
        }
    }
}
