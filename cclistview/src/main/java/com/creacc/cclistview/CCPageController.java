package com.creacc.cclistview;

/**
 * Created by Administrator on 2016/3/3.
 */
public class CCPageController {

    private static final int INVALID_PAGE_INDEX = -1;

    private static final int FIRST_PAGE_INDEX = 0;

    private CCListViewProxy mListViewProxy;

    private int mPageIndex;

    private int mMinPageIndex = INVALID_PAGE_INDEX;

    private int mMaxPageIndex = INVALID_PAGE_INDEX;

    private int mFinalPageIndex = INVALID_PAGE_INDEX;

    private int mMaxCountPerPage;

    public CCPageController(CCListViewProxy listViewProxy, int maxCountPerPage) {
        this(listViewProxy, FIRST_PAGE_INDEX, maxCountPerPage);
    }

    public CCPageController(CCListViewProxy listView, int page, int maxCountPerPage) {
        mListViewProxy = listView;
        mPageIndex = page;
        mMinPageIndex = page;
        mMaxPageIndex = page;
        mMaxCountPerPage = maxCountPerPage;
        mListViewProxy.setMode(page == FIRST_PAGE_INDEX ? CCListViewProxy.PULL_FROM_END :
                CCListViewProxy.PULL_FROM_BOTH);
    }

    public Page initializePage() {
        reset(mPageIndex);
        return new Page(mPageIndex, this, true);
    }

    public Page nextPage() {
        if (mPageIndex == mFinalPageIndex) {
            return new Page(mPageIndex, this, false);
        } else {
            if (mMaxPageIndex != INVALID_PAGE_INDEX && mPageIndex <= mMaxPageIndex) {
                return new Page(++mPageIndex, this, false);
            } else {
                return new Page(mPageIndex + 1, this, true);
            }
        }
    }

    public Page prevPage() {
        if (mPageIndex == 0) {
            return new Page(mPageIndex, this, false);
        } else {
            if (mMinPageIndex != INVALID_PAGE_INDEX && mPageIndex >= mMinPageIndex) {
                return new Page(--mPageIndex, this, false);
            } else {
                return new Page(mPageIndex - 1, this, true);
            }
        }
    }

    public boolean isFirstPage() {
        return mPageIndex == 0;
    }

    public boolean isFinalPage() {
        return mFinalPageIndex != INVALID_PAGE_INDEX && mPageIndex == mFinalPageIndex;
    }

    public void reset() {
        reset(FIRST_PAGE_INDEX);
    }

    public void reset(int page) {
        mPageIndex = page;
        mMinPageIndex = INVALID_PAGE_INDEX;
        mMaxPageIndex = INVALID_PAGE_INDEX;
        mFinalPageIndex = INVALID_PAGE_INDEX;
    }

    private void update(int page, int countPerPage) {
        if (countPerPage > 0) {
            mPageIndex = page;
            if (mMaxPageIndex == INVALID_PAGE_INDEX || mMaxPageIndex < page) {
                mMaxPageIndex = page;
            }
            if (mMinPageIndex == INVALID_PAGE_INDEX || mMinPageIndex > page) {
                mMinPageIndex = page;
            }
            if (countPerPage < mMaxCountPerPage) {
                mFinalPageIndex = page;
            }
        } else {
            mFinalPageIndex = mPageIndex;
        }
    }

    private void complete() {
        mListViewProxy.completeRefresh();
        if (isFinalPage() && isFinalPage()) {
            mListViewProxy.setMode(CCListViewProxy.PULL_FROM_NONE);
        } else {
            if (isFirstPage()) {
                mListViewProxy.setMode(CCListViewProxy.PULL_FROM_END);
            } else if (isFinalPage()) {
                mListViewProxy.setMode(CCListViewProxy.PULL_FROM_START);
            } else {
                mListViewProxy.setMode(CCListViewProxy.PULL_FROM_BOTH);
            }
        }
    }

    public class Page {

        private int mPageIndex;

        private boolean mIsNewPage;

        private CCPageController mCCPageController;

        private Page(int page, CCPageController controller, boolean isNewPage) {
            mPageIndex = page;
            mCCPageController = controller;
            mIsNewPage = isNewPage;
        }

        public int value() {
            return mPageIndex;
        }

        public void apply(int countPerPage) {
            if (mIsNewPage) {
                mCCPageController.update(mPageIndex, countPerPage);
            }
            mCCPageController.complete();
        }
    }
}
