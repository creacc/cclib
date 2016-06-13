package com.creacc.cclistview;

/**
 * Created by Administrator on 2016/3/6.
 */
public interface CCListViewProxy {

    int PULL_FROM_BOTH = 0;

    int PULL_FROM_NONE = -1;

    int  PULL_FROM_START = 1;

     int PULL_FROM_END = 2;

    void setMode(int mode);

    void completeRefresh();
}
