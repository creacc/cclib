package com.creacc.ccbox.utils;

/**
 * Created by Administrator on 2015/7/26.
 */
public abstract class Replacement<ReplaceType, SourceType> {

    private ReplaceType mReplaceElement;

    private SourceType mSourceElement;

    public void replace(ReplaceType element) {
        synchronized (this) {
            if (this.mSourceElement != null) {
                this.mReplaceElement = element;
            } else {
                this.mSourceElement = this.onReplace(element);
            }
        }
    }

    public void cancel() {
        synchronized (this) {
            if (this.mSourceElement != null) {
                this.onCancel(this.mSourceElement);
            }
            if (this.mReplaceElement != null) {
                this.mSourceElement = this.onReplace(this.mReplaceElement);
                this.mReplaceElement = null;
            } else {
                this.mSourceElement = null;
            }
        }
    }

    public abstract SourceType onReplace(ReplaceType replace);

    public abstract void onCancel(SourceType source);
}
