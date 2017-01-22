package com.ifttt.sparklemotion.animations;

import android.view.View;

import com.ifttt.sparklemotion.Animation;
import com.ifttt.sparklemotion.Page;

/**
 * {@link Animation} subclass that simply takes the <code>offset</code> from
 * {@link #onAnimate(View, float, float)} and apply that to the target view, so that the view will
 * stay where it is during the page scrolling.
 */
public class NoMovementAnimation extends Animation {

    /**
     * Constructor for building an NoMovementAnimation for a range of pages.
     *
     * @param page Page object newBuilder specific page information about this animation.
     */
    public NoMovementAnimation() {
        super();
    }

    @Override
    public void onAnimate(View v, float offset, float offsetInPixel) {
        v.setTranslationX(offsetInPixel);
    }
}
