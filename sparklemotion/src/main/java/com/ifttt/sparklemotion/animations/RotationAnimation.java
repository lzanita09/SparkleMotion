package com.ifttt.sparklemotion.animations;

import android.view.View;

import com.ifttt.sparklemotion.Animation;

/**
 * Subclass of {@link Animation} that changes View's rotation.
 */
public class RotationAnimation extends Animation {

    private final float inRotation;
    private final float outRotation;

    /**
     * Constructor for building a RotationAnimation for a range of pages.
     *
     * @param inRotation  Rotation value when the page of the View is currently primary page.
     * @param outRotation Rotation value when the page of the View is not visible, i.e. the page is scrolled to either
     *                    left or right of the primary page.
     */
    public RotationAnimation(float inRotation, float outRotation) {
        super();

        this.inRotation = inRotation;
        this.outRotation = outRotation;
    }

    @Override
    public void onAnimate(View v, float offset, float offsetInPixel) {
        offset = Math.abs(offset);

        v.setRotation(inRotation + offset * (outRotation - inRotation));
    }
}
