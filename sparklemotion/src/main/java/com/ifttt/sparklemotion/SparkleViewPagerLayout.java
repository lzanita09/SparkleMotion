package com.ifttt.sparklemotion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * A wrapper FrameLayout containing a {@link ViewPager}. This class supports adding
 * {@link Decor} to the ViewPager, which can be animated across pages.
 * <p/>
 * A Decor of the ViewPager is only for animation purpose, which means if there's no animation
 * associated newBuilder it, it will stay at the original position when it is added to the parent.
 */
public class SparkleViewPagerLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    public SparkleViewPagerLayout(Context context) {
        super(context);
    }

    public SparkleViewPagerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SparkleViewPagerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof ViewPager) {
            setViewPager((ViewPager) child);
        }

        super.addView(child, index, params);
    }

    /**
     * Setup a ViewPager within this layout, so that we can use it to run animations.
     *
     * @param viewPager ViewPager object being added to this layout.
     */
    private void setViewPager(@NonNull ViewPager viewPager) {
        if (this.viewPager != null) {
            throw new IllegalStateException("SparkleViewPagerLayout already has a ViewPager set.");
        }

        if (!SparkleMotionCompat.hasPresenter(viewPager)) {
            SparkleMotionCompat.installAnimationPresenter(viewPager);
        }

        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(this);
    }

    /**
     * Return the {@link ViewPager} used in this layout.
     *
     * @return ViewPager object.
     */
    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        enableLayer(state != ViewPager.SCROLL_STATE_IDLE);
    }

    /**
     * If the ViewPager is scrolling and there are Decors that are running animations, enable their
     * content Views' hardware layer. Otherwise, switch back to no layer.
     *
     * @param enable Whether or not hardware layer should be used for Decor content views.
     */
    private void enableLayer(boolean enable) {
        final int layerType = enable ? LAYER_TYPE_HARDWARE : LAYER_TYPE_NONE;
//        for (Decor decor : decors) {
//            if (decor.withLayer) {
//                decor.contentView.setLayerType(layerType, null);
//            }
//        }
    }
}
