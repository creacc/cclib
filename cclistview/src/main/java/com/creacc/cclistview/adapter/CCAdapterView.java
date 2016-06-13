package com.creacc.cclistview.adapter;

import android.view.View;

/**
 * Created by apple on 2015/12/12.
 */
public interface CCAdapterView<ContentType> {

    int getResource();

    void initializeView(View rootView);

    void fillContent(ContentType content, int index);
}