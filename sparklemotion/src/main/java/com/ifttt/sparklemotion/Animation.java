package com.ifttt.sparklemotion;

import android.view.View;
import android.view.animation.Interpolator;

/**
 * Abstract class for running SparkleMotion animation. This class contains all common information about
 * the animation to be run on ViewPager pages, newBuilder an abstract method
 * {@link #onAnimate(View, float, float)} to be implemented so that the View properties can be
 * changed during ViewPager scrolling.
 */
public abstract class Animation {

    private Interpolator interpolator;

    private AnimationListener animationListener;

    private Page page;

    /**
     * Base constructor of the class, accepting common information about the animation to this
     * instance.
     *
     * For animations that will run on multiple pages, the progress of the animation will be evenly split
     * across the pages. For ViewPager View animation, it might not be necessary to run such animation, as the View
     * will be invisible once the page is scrolled off-screen.
     *
     */
    public Animation() {
    }

    public final void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    void setPage(Page page) {
        this.page = page;
    }

    Page getPage() {
        return page == null ? Page.allPages() : page;
    }

    /**
     * Main method for animating Views within the pages.
     *
     * @param v             View to be animated.
     * @param offset        Fraction of the ViewPager scrolling, this is also the progression of the
     *                      animation.
     * @param offsetInPixel Page width offset.
     */
    void animate(View v, float offset, float offsetInPixel) {
        if (interpolator != null) {
            offset = interpolator.getInterpolation(offset);
        }

        if (offset < -1) {
            onAnimateOffScreenLeft(v, offset, offsetInPixel);
        } else if (offset <= 1) {
            onAnimate(v, offset, offsetInPixel);
        } else {
            onAnimateOffScreenRight(v, offset, offsetInPixel);
        }

        if (animationListener != null) {
            animationListener.onAnimationRunning(v, offset);
        }
    }

    /**
     * Abstract method to be implemented to change View properties. Implement this method to
     * provide custom animations to the target View. This method will be called when the page is
     * still visible within the ViewPager. Range [-1, 0] means the page is currently scrolling
     * to the left of the window, and [0, 1] means the page is currently scrolling to the right of
     * the window.
     *
     *
     * @param v             View being animated.
     * @param offset        Fraction of the ViewPager scrolling, this is also the progression of
     *                      the
     *                      animation, the
     *                      range of the offset is [-1, 1].
     * @param offsetInPixel Page width offset.
     */
    public abstract void onAnimate(View v, float offset, float offsetInPixel);

    /**
     * Called when the animation is running when the View is off screen and is to the left of the
     * current screen.
     *
     * This method is called only for Views inside ViewPager.
     *
     * @param v             View being animated.
     * @param offset        Fraction of the ViewPager scrolling, this is also the progression of
     *                      the
     *                      animation.
     * @param offsetInPixel Page width offset.
     */
    @SuppressWarnings("unused")
    public void onAnimateOffScreenLeft(View v, float offset, float offsetInPixel) {
    }

    /**
     * Called when the animation is running when the View is off screen and is to the right of the
     * current screen.
     *
     * This method is called only for Views inside ViewPager.
     *
     * @param v             View being animated.
     * @param offset        Fraction of the ViewPager scrolling, this is also the progression of
     *                      the
     *                      animation.
     * @param offsetInPixel Page width offset.
     */
    @SuppressWarnings("unused")
    public void onAnimateOffScreenRight(View v, float offset, float offsetInPixel) {
    }

    /**
     * Set an {@link AnimationListener} for this animation.
     *
     * @param listener AnimationListener object.
     */
    @SuppressWarnings("unused")
    public void setAnimationListener(AnimationListener listener) {
        animationListener = listener;
    }

    /**
     * Animation callback interface for external use.
     */
    public interface AnimationListener {
        /**
         * Called when the animation is running.
         *
         * @param view     View being animated.
         * @param fraction Current fraction of the animation.
         */
        void onAnimationRunning(View view, float fraction);
    }
}
