package com.creacc.cclistview.adapter;

import android.view.View;

/**
 * Created by yanhaifeng on 16-7-27.
 */

public interface CCAdapterHolder<T> {

    /**
     * Decide which layout to display.
     *
     * @return Item layout id.
     */
    int getResource();

    /**
     * Initialize views in layout.
     *
     * @param convertView Layout view.
     * @param position Item position.
     */
    void initializeView(View convertView, int position);

    /**
     * Update layout views by content.
     *
     * @param content Item content.
     * @param position Item position.
     */
    void updateView(T content, int position);
}
