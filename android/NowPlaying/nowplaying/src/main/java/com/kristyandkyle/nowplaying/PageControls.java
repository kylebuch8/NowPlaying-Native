package com.kristyandkyle.nowplaying;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by kylebuchanan on 1/4/14.
 */
public class PageControls extends LinearLayout implements PageIndicator {

    private LinearLayout mLayout;
    private ViewPager mViewPager;
    private int mNumPages;
    private int mCurrentPage = 0;
    private Runnable mPageSelector;
    private ViewPager.OnPageChangeListener mListener;

    public PageControls(Context context) {
        this(context, null);
    }

    public PageControls(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PageControls,
                0, 0);

        try {
            mNumPages = a.getInteger(R.styleable.PageControls_numPages, 1);
        } finally {
            a.recycle();
        }

        mLayout = this;
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }

        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }

        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance");
        }

        mViewPager = view;
        mViewPager.setOnPageChangeListener(this);

        mNumPages = mViewPager.getAdapter().getCount();

        notifyDataSetChanged();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    public void notifyDataSetChanged() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < mNumPages; i++) {
            ImageView image = (ImageView) inflater.inflate(R.layout.page_control, this, false);

            if (i == mNumPages - 1 && mNumPages > 1) {
                LayoutParams layoutParams = (LayoutParams) image.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                image.setLayoutParams(layoutParams);
            }

            addView(image);
        }

        setCurrentItem(mCurrentPage);
        requestLayout();
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound");
        }

        mViewPager.setCurrentItem(item);
        mCurrentPage = item;

        for (int i = 0; i < mNumPages; i++) {
            final ImageView imageView = (ImageView) mLayout.getChildAt(i);
            final int alpha;

            if (i == mCurrentPage) {
                alpha = 255;
            } else {
                alpha = 125;
            }

            mPageSelector = new Runnable() {

                @Override
                public void run() {
                    imageView.getDrawable().setAlpha(alpha);
                }
            };

            post(mPageSelector);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

}
