package com.ifttt.sparklemotion.animations;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ifttt.sparklemotion.Animation;

/**
 * {@link Animation} subclass used for running slide out animation when {@link Decor.Builder#slideIn()} is used.
 * Note that this animation has state, therefore cannot be reused by other Views, each View needs to have
 * a new instance of it.
 */
public class SlideInAnimation extends Animation {

    private boolean originalTranslationSet;

    /**
     * Distance of the View to slide out of the screen.
     */
    private float distance;

    /**
     * Translation X of the View before running this animation.
     */
    private float originalTranslationX;

    public SlideInAnimation() {
        super();
    }

    @Override
    public void onAnimate(View view, float offset, float offsetInPixel) {
        if (!originalTranslationSet) {
            originalTranslationSet = true;
            initViewPosition(view, 1 - offset);
        } else {
            offset = Math.abs(offset);
            view.setTranslationX(originalTranslationX + (1 - offset) * distance);
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
        View parent = (View) view.getParent();
        if (parent == null) {
            return;
        }

        if (ViewCompat.isLaidOut(view)) {
            originalTranslationX = view.getTranslationX();
            distance = parent.getWidth() - view.getLeft();

            // Once initialized, run the initial animation frame.
            view.setTranslationX(originalTranslationX + (1 - Math.abs(offset)) * distance);
            return;
        }

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                originalTranslationX = view.getTranslationX();
                View parent = (View) view.getParent();
                if (parent == null) {
                    return false;
                }

                distance = parent.getWidth() - view.getLeft();

                // Once initialized, run the initial animation frame.
                view.setTranslationX(originalTranslationX + (1 - Math.abs(offset)) * distance);
                return false;
            }
        });
    }
}
