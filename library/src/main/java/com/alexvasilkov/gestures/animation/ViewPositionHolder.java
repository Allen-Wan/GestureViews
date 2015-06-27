package com.alexvasilkov.gestures.animation;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

class ViewPositionHolder implements ViewTreeObserver.OnGlobalLayoutListener {

    private final ViewPosition mPos = ViewPosition.newInstance();

    private OnViewPositionChangeListener mListener;
    private View mView;

    @Override
    public void onGlobalLayout() {
        if (mView != null && mListener != null) {
            boolean changed = ViewPosition.from(mPos, mView);
            if (changed) mListener.onViewPositionChanged(mPos);
        }
    }

    public void init(@NonNull View view, @NonNull OnViewPositionChangeListener listener) {
        mView = view;
        mListener = listener;
        mView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        if (isLaidOut()) onGlobalLayout();
    }

    @SuppressWarnings("deprecation")
    public void destroy() {
        if (mView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                mView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }

        mPos.view.setEmpty();
        mPos.viewport.setEmpty();
        mPos.image.setEmpty();

        mView = null;
        mListener = null;
    }

    private boolean isLaidOut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return mView.isLaidOut();
        } else {
            return mView.getWidth() > 0 && mView.getHeight() > 0;
        }
    }

    public interface OnViewPositionChangeListener {
        void onViewPositionChanged(@NonNull ViewPosition position);
    }

}