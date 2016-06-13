package com.creacc.ccdrag;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/4/20.
 */
public class DragDetector implements View.OnTouchListener {

    public enum DragOrientation {

        HORIZONTAL(true, false),
        VERTICAL(false, true),
        BOTH(true, true);

        private boolean isHorizontalSupported;

        private boolean isVerticalSupported;

        DragOrientation(boolean h, boolean v) {
            isHorizontalSupported = h;
            isVerticalSupported = v;
        }
    }

    public enum DragMode {

        HARD,
        ELASTIC;

        DragMode() {

        }
    }

    private boolean mIsTouchDown;

    private boolean mIsTouchMove;

    private PointF mTouchDownPoint = new PointF();

    private DragOrientation mDragOrientation;

    private DragMode mDragMode;

    private Scroller mScroller;

    public DragDetector() {
        this(DragOrientation.BOTH);
    }

    public DragDetector(DragMode mode) {
        this(DragOrientation.BOTH, mode);
    }

    public DragDetector(DragOrientation orientation) {
        this(orientation, DragMode.HARD);
    }

    public DragDetector(DragOrientation orientation, DragMode mode) {
        mDragOrientation = orientation;
        mDragMode = mode;
    }

    public void setDragMode(DragMode mode) {
        this.mDragMode = mode;
    }

    public void setFixedX(int x, int y) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsTouchDown = true;
                mTouchDownPoint.set(motionEvent.getRawX(), motionEvent.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsTouchDown) {
                    mIsTouchMove = true;
                    int xOffset = mDragOrientation.isHorizontalSupported ?
                            (int) (motionEvent.getRawX() - mTouchDownPoint.x) : 0;
                    int yOffset = mDragOrientation.isVerticalSupported ?
                            (int) (motionEvent.getRawY() - mTouchDownPoint.y) : 0;
                    view.layout(
                            view.getLeft() + xOffset,
                            view.getTop() + yOffset,
                            view.getRight() + xOffset,
                            view.getBottom() + yOffset
                    );
                    mTouchDownPoint.set(motionEvent.getRawX(), motionEvent.getRawY());
                    return true;
                }
            case MotionEvent.ACTION_UP:
                mIsTouchDown = false;
                if (mIsTouchMove) {
                    mIsTouchMove = false;
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

}
