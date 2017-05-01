package com.creacc.cclistview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.creacc.cclistview.R;

import java.util.List;

/**
 * Created by yanhaifeng on 16-8-5.
 */

public abstract class CCGridAdapter<T> extends CCBaseAdapter<T> {

    private int mColumnCount;

    private ListView mListView;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    private OnGridItemClickListener mOnGridItemClickListener = new OnGridItemClickListener();

    private OnGridItemLongClickListener mOnGridItemLongClickListener = new OnGridItemLongClickListener();

    private RowLayoutGenerator mRowLayoutGenerator;

    public CCGridAdapter(Context context, List<T> adapterContent, int columnCount) {
        super(context, adapterContent);
        initializeColumnCount(columnCount);
        mRowLayoutGenerator = createRowLayoutGenerator();
    }

    CCGridAdapter(CCBaseAdapter<T> adapter, int columnCount, RowLayoutGenerator generator) {
        super(adapter.getContext(), adapter.getItems());
        initializeColumnCount(columnCount);
        if (generator == null) {
            generator = createRowLayoutGenerator();
        }
        mRowLayoutGenerator = generator;
    }

    @Override
    public final int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public final int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return getRowCount(super.getCount(), mColumnCount);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int childCount = super.getCount();
        LinearLayout rowLayout = (LinearLayout) convertView;
        if (rowLayout == null) {
            rowLayout = mRowLayoutGenerator.createRowLayout(getContext(), parent);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            convertView = rowLayout;
            for (int i = 0; i < mColumnCount; i++) {
                View childView;
                int childIndex = position * mColumnCount + i;
                if (childIndex < childCount) {
                    childView = createView(childIndex, rowLayout);
                    childView.setTag(R.string.le_adapter_holder_index_tag_key, childIndex);
                    updateView(childIndex, childView);
                } else {
                    childView = createView(0, rowLayout);
                    childView.setVisibility(View.INVISIBLE);
                    childView.setEnabled(false);
                }
                childView.setOnClickListener(mOnGridItemClickListener);
                childView.setOnLongClickListener(mOnGridItemLongClickListener);
                rowLayout.addView(childView);
            }
        } else {
            for (int i = 0; i < mColumnCount; i++) {
                View childView = rowLayout.getChildAt(i);
                int childIndex = position * mColumnCount + i;
                if (childIndex < childCount) {
                    childView.setVisibility(View.VISIBLE);
                    childView.setEnabled(true);
                    childView.setTag(R.string.le_adapter_holder_index_tag_key, childIndex);
                    updateView(childIndex, childView);
                } else {
                    childView.setVisibility(View.INVISIBLE);
                    childView.setEnabled(false);
                }
            }
        }
        return convertView;
    }

    @Override
    public void updateView(int position, View convertView) {
        super.updateView(position, convertView);
        convertView.setTag(R.string.le_adapter_holder_index_tag_key, position);
    }

    protected RowLayoutGenerator createRowLayoutGenerator() {
        return new RowLayoutGeneratorImpl();
    }

    private int getRowCount(int dataCount, int columnCount) {
        int count = dataCount / columnCount;
        if (dataCount % columnCount > 0) {
            count++;
        }
        return count;
    }

    @Override
    public void setOnItemClickListener(ListView listView, AdapterView.OnItemClickListener listener) {
        mListView = listView;
        mOnItemClickListener = listener;
    }

    @Override
    public void setOnItemLongClickListener(ListView listView, AdapterView.OnItemLongClickListener listener) {
        mListView = listView;
        mOnItemLongClickListener = listener;
    }

    @Override
    public void updateItemView(ListView listView, int index) {
        int firstVisibleItemIndex = listView.getFirstVisiblePosition() * mColumnCount;
        int lastVisibleItemIndex = (listView.getLastVisiblePosition() + 1) * mColumnCount - 1;
        if (index >= firstVisibleItemIndex && index <= lastVisibleItemIndex) {
            int offset = index - firstVisibleItemIndex;
            int rowIndex = offset / mColumnCount + listView.getHeaderViewsCount();
            LinearLayout rowLayout = (LinearLayout) listView.getChildAt(rowIndex);
            updateView(index, rowLayout.getChildAt(offset % mColumnCount));
        }
    }

    private void initializeColumnCount(int columnCount) {
        if (columnCount < 2) {
            throw new IllegalArgumentException("The column count must more than 1.");
        }
        mColumnCount = columnCount;
    }

    private class OnGridItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null && mListView != null) {
                int position = (Integer) view.getTag(R.string.le_adapter_holder_index_tag_key);
                mOnItemClickListener.onItemClick(mListView, view, position, getItemId(position));
            }
        }
    }

    private class OnGridItemLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {
            if (mOnItemLongClickListener != null && mListView != null) {
                int position = (Integer) view.getTag(R.string.le_adapter_holder_index_tag_key);
                return mOnItemLongClickListener.onItemLongClick(mListView, view, position, getItemId(position));
            }
            return false;
        }
    }

    public interface RowLayoutGenerator {

        LinearLayout createRowLayout(Context context, ViewGroup parent);
    }

    protected class RowLayoutGeneratorImpl implements RowLayoutGenerator {

        @Override
        public LinearLayout createRowLayout(Context context, ViewGroup parent) {
            return new LinearLayout(context);
        }
    }
}
