package com.mercadolibre.jvillarnovo.trainingpractico1.Adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadolibre.jvillarnovo.trainingpractico1.R;
import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;
import com.mercadolibre.jvillarnovo.trainingpractico1.handler.MainThreadHandler;
import com.mercadolibre.jvillarnovo.trainingpractico1.manager.ImageDownloadManager;
import com.mercadolibre.jvillarnovo.trainingpractico1.task.AsyncSearch;
import com.mercadolibre.jvillarnovo.trainingpractico1.util.Closure;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jvillarnovo on 06/11/14.
 */
public class ListViewAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    private LayoutInflater inflater;
    private List<Item> items;
    private AsyncSearch search;
    private String itemSearch;
    private View rowLoadingView;
    private int lastOffsetRequested;

    public LinkedList<Item> getListItems(){
        return (LinkedList<Item>) items;
    }

    public ListViewAdapter(LayoutInflater inflater,List<Item> items, String itemSearch){
        this.inflater=inflater;
        this.items=items;
        this.itemSearch=itemSearch;
        rowLoadingView=inflater.inflate(R.layout.row_loading, null);
    }

    @Override
    public int getCount() {
        if (items == null) {
            return 0;
        } else {
            return items.size();
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

        if(i==items.size()-1){
            return rowLoadingView;
        }

        if(view==null || view==rowLoadingView){
            view=inflater.inflate(R.layout.row_item,null);
            holder=new ItemViewHolder();
            holder.title=(TextView) view.findViewById(R.id.txtTitle);
            holder.price=(TextView) view.findViewById(R.id.txtPrice);
            holder.imageView=(ImageView) view.findViewById(R.id.thumbnailView);

            View subtitleView = view.findViewById(R.id.txtSubtitle);
            if(subtitleView !=null){
                holder.subtitle=(TextView) subtitleView;
            }

            View stockView = view.findViewById(R.id.txtStock);
            if(stockView !=null){
                holder.stock=(TextView) stockView;
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
            if(item.getSubtitle()==null){
                holder.subtitle.setText("");
            } else {
                holder.subtitle.setText(item.getSubtitle());
            }
        }

        if(item.getImg()==null){
            holder.imageView.setImageDrawable(viewGroup.getResources().getDrawable(R.drawable.icon_loading));
            if(!item.isDownloadImg()){
                item.setDownloadImg(true);
                if(!item.getImgUrl().isEmpty()) {
                    sendRequestThumbnail(item.getImgUrl(), i);
                }
            }
        } else {
            holder.imageView.setImageBitmap(item.getImg());
        }

        return view;
    }

    public void updateData(LinkedList<Item> list){
        items.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateImgItem(int itemIndex, Bitmap img) {
        items.get(itemIndex).setImg(img);
        notifyDataSetChanged();
    }

    public void sendRequestItems(int limit, int offset){
        lastOffsetRequested=offset;
        search=new AsyncSearch(itemSearch,limit,offset, new Closure<LinkedList<Item>>() {
            @Override
            public void executeOnSuccess(LinkedList<Item> result) {
                updateData(result);
                search=null;
            }
        });
    }

    public void sendRequestThumbnail(String imgUrl, int itemIndex){
        ImageDownloadManager.getInstance(MainThreadHandler.getInstance(this)).getImage(imgUrl, itemIndex);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int lastVisibleItem=firstVisibleItem+visibleItemCount;
        int offset=getCount();

        if(lastVisibleItem>offset-5 && search==null && lastOffsetRequested<offset && offset>0){
            //TODO solucionar los request repetidos con el mismo offset
            //Log.d("ListViewAdapter.onScroll()","offset= "+offset+", lastOffset= "+
            //        lastOffsetRequested+", search= "+(search!=null?"inProgress":"null"));
            sendRequestItems(16, offset);
        }
    }

}
