package com.ifttt.sparklemotion.animations;

import android.view.View;

import com.ifttt.sparklemotion.Animation;

/**
 * Subclass of {@link Animation} that changes alpha of the View.
 */
public class AlphaAnimation extends Animation {

    private final float outAlpha;
    private final float inAlpha;

    /**
     * Constructor for building an AlphaAnimation for a range of pages.
     *
     * @param outAlpha Alpha value when the View is not visible, i.e. the page is scrolled to the left or right
     *                 of the primary page.
     * @param inAlpha  Alpha value when the View is visible as the primary page.
     */
    public AlphaAnimation(float inAlpha, float outAlpha) {
        super();

        this.inAlpha = inAlpha;
        this.outAlpha = outAlpha;
    }

    @Override
    public void onAnimate(View v, float offset, float offsetInPixel) {
        offset = Math.abs(offset);
        v.setAlpha(inAlpha + offset * (outAlpha - inAlpha));
    }
}
