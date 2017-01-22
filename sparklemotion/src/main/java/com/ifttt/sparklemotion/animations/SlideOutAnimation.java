package com.ifttt.sparklemotion.animations;

import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ifttt.sparklemotion.Animation;

/**
 * {@link Animation} subclass used for running slide out animation when {@link Decor.Builder#slideOut()} is used.
 * Note that this animation has state, therefore cannot be reused by other Views, each View needs to have
 * a new instance of it.
 */
public class SlideOutAnimation extends Animation {

    private boolean originalTranslationSet;

    /**
     * Distance of the View to slide out of the screen.
     */
    private float distance;

    /**
     * Translation X of the View before running this animation.
     */
    private float originalTranslationX;

    public SlideOutAnimation() {
        super();
    }

    @Override
    public void onAnimate(final View view, float offset, float offsetInPixel) {
        if (!originalTranslationSet) {
            originalTranslationSet = true;
            initViewPosition(view, offset);
        } else {
            offset = Math.abs(offset);
            view.setTranslationX(originalTranslationX + offset * distance);

            Log.d(SlideOutAnimation.class.getSimpleName(), view + " " + view.getTranslationX() + " ");
        }
    }

    /**
     * Initialize the View's position by adding a {@link android.view.ViewTreeObserver.OnPreDrawListener}, listening to
     * pre-draw, then assign the initial frame and destination frame.
     *
     * @param view   View to be animated.
     * @param offset Initial offset.
     */
    private void initViewPosition(final View view, final float offset) {
        if (ViewCompat.isLaidOut(view)) {
            originalTranslationX = view.getTranslationX();
            distance = -(view.getLeft() + view.getWidth() * view.getScaleX());

            // Once initialized, run the initial animation frame.
            view.setTranslationX(originalTranslationX + Math.abs(offset) * distance);
        } else {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    originalTranslationX = view.getTranslationX();
                    distance = -(view.getLeft() + view.getWidth() * view.getScaleX());

                    // Once initialized, run the initial animation frame.
                    view.setTranslationX(originalTranslationX + Math.abs(offset) * distance);
                    return false;
                }
            });
        }
    }
}
