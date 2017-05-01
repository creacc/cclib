package com.creacc.cclistview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.creacc.cclistview.R;

import java.util.List;

/**
 * Created by yanhaifeng on 16-7-27.
 */

public abstract class CCBaseAdapter<T> extends BaseAdapter {

    private List<T> mAdapterContent;

    private Context mContext;

    private LayoutInflater mLayoutInflater;

    /**
     * Construct base adapter.
     *
     * @param context Context.
     * @param adapterContent Content to display.
     */
    public CCBaseAdapter(Context context, List<T> adapterContent) {
        if (adapterContent != null && adapterContent instanceof CCAdapterHandler) {
            ((CCAdapterHandler) adapterContent).setContentObserver(new CCAdapterHandler.ContentObserver() {
                @Override
                public void onContentChanged() {
                    notifyDataSetChanged();
                }
            });
        }
        mAdapterContent = adapterContent;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * set adapterContent.
     *
     * @param adapterContent Content to display.
     */
    public void setAdapterContent(List<T> adapterContent) {
        this.mAdapterContent = adapterContent;
    }

    /**
     * To be a grid adapter with the same item layout.
     *
     * @param columnCount Column count.
     * @return Grid adapter
     */
    public CCGridAdapter asGridAdapter(int columnCount) {
        return asGridAdapter(columnCount, null);
    }

    /**
     * To be a grid adapter with the same item layout.
     *
     * @param columnCount Column count.
     * @param generator Row layout generator.
     * @return Grid adapter
     */
    public CCGridAdapter asGridAdapter(int columnCount, CCGridAdapter.RowLayoutGenerator generator) {
        return new CCGridAdapter(this, columnCount, generator) {
            @Override
            protected CCAdapterHolder createHolder(int type) {
                return CCBaseAdapter.this.createHolder(type);
            }
        };
    }

    /**
     * Set item click listener of grid, instead the method of ListView.
     *
     * @param listView ListView to listen.
     * @param listener Grid item listener.
     */
    public void setOnItemClickListener(ListView listView, AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }

    /**
     * Set item long click listener of grid, instead the method of ListView.
     *
     * @param listView ListView to listen.
     * @param listener Grid item listener.
     */
    public void setOnItemLongClickListener(ListView listView, AdapterView.OnItemLongClickListener listener) {
        listView.setOnItemLongClickListener(listener);
    }

    /**
     * To update a item view, avoid calling {@link BaseAdapter#notifyDataSetChanged()} to refresh all items.
     *
     * @param listView ListView to update item.
     * @param index Item index.
     */
    public void updateItemView(ListView listView, int index) {
        int firstVisibleItemIndex = listView.getFirstVisiblePosition();
        int lastVisibleItemIndex = listView.getLastVisiblePosition();
        if (index >= firstVisibleItemIndex && index <= lastVisibleItemIndex) {
            View view = listView.getChildAt(index - firstVisibleItemIndex);
            updateView(index, view);
        }
    }

    @Override
    public int getCount() {
        if (mAdapterContent == null) {
            return 0;
        }
        return mAdapterContent.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapterContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createView(position, parent);
        }
        updateView(position, convertView);
        return convertView;
    }

    @NonNull
    protected final View createView(int position, ViewGroup parent) {
        CCAdapterHolder<T> holder = createHolder(getItemViewType(position));
        View view = createView(parent, holder, position);
        if (view != null) {
            holder.initializeView(view, position);
            view.setTag(R.string.le_adapter_holder_tag_key, holder);
            return view;
        }
        return view;
    }

    protected View createView(ViewGroup parent, CCAdapterHolder<T> holder, int position) {
        return mLayoutInflater.inflate(holder.getResource(), parent, false);
    }

    protected void updateView(int position, View convertView) {
        CCAdapterHolder<T> holder = getHolder(convertView);
        if (holder != null) {
            holder.updateView((T) getItem(position), position);
        }
    }

    public CCAdapterHolder<T> getHolder(View convertView) {
        return (CCAdapterHolder<T>) convertView.getTag(R.string.le_adapter_holder_tag_key);
    }

    protected Context getContext() {
        return mContext;
    }

    protected LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    public List<T> getItems() {
        return mAdapterContent;
    }

    /**
     * Create the holder of item layout base on the type.
     *
     * @param type Item type define in {@link BaseAdapter#getItemViewType(int)}
     * @return Holder with item layout.
     */
    protected abstract CCAdapterHolder<T> createHolder(int type);
}
