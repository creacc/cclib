package com.creacc.cclistview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.creacc.cclistview.R;

/**
 * Created by apple on 2015/12/12.
 */
public abstract class CCAdapter<ContentType> extends BaseAdapter {

    private Context mContext;

    private View mLastClickView;

    private CCAdapterContent<ContentType> mAdapterContent;

    public CCAdapter(Context context, CCAdapterContent<ContentType> content) {
        mContext = context;
        mAdapterContent = content;
        mAdapterContent.setObserver(new CCAdapterContent.ContentObserver() {
            @Override
            public void onContentChanged() {
                notifyDataSetChanged();
            }
        });
    }

    public CCAdapterContent<ContentType> getAdapterContent() {
        return mAdapterContent;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mAdapterContent.count();
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
            CCAdapterView adapterView = createAdapterView();
            int resource = adapterView.getResource();
            convertView = LayoutInflater.from(mContext).inflate(resource, null);
            adapterView.initializeView(convertView);
            convertView.setTag(R.string.adapter_tag_view, adapterView);
        }
        ((CCAdapterView) convertView.getTag(R.string.adapter_tag_view)).fillContent(getItem(position), position);
        return convertView;
    }

    public abstract CCAdapterView<ContentType> createAdapterView();

}
