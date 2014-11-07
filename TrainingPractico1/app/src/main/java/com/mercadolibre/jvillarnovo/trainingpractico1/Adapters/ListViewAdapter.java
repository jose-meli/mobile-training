package com.mercadolibre.jvillarnovo.trainingpractico1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mercadolibre.jvillarnovo.trainingpractico1.R;
import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by jvillarnovo on 06/11/14.
 */
public class ListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Item> items;

    public ListViewAdapter(LayoutInflater inflater,List<Item> items){
        this.inflater=inflater;
        this.items=items;
    }

    @Override
    public int getCount() {
        if(items!=null){
            return items.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemViewHolder holder;

        if(view==null){
            view=inflater.inflate(R.layout.row_item,null);
            holder=new ItemViewHolder();
            holder.title=(TextView) view.findViewById(R.id.txtTitle);
            holder.price=(TextView) view.findViewById(R.id.txtPrice);

            if(view.findViewById(R.id.txtSubtitle)!=null){
                holder.subtitle=(TextView) view.findViewById(R.id.txtSubtitle);
            }

            if(view.findViewById(R.id.txtStock)!=null){
                holder.stock=(TextView) view.findViewById(R.id.txtStock);
            }

            view.setTag(holder);
        } else {
            holder=(ItemViewHolder) view.getTag();
        }

        Item item=items.get(i);

        holder.title.setText(item.getTitle());
        holder.price.setText("$ "+ NumberFormat.getInstance().format(item.getPrice()));

        if (holder.stock != null) {
            if (item.getStock() == null) {
                holder.stock.setText(null);
            } else {
                String stock = view.getContext().getString(R.string.stock);
                holder.stock.setText(stock + ": " + item.getStock());
            }
        }

        if(holder.subtitle!=null){
            if(item.getSubtitle()!=null){
                holder.subtitle.setText(item.getSubtitle());
            } else {
                holder.subtitle.setText("");
            }
        }

        return view;
    }
}
