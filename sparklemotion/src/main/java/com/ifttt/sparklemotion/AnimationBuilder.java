package com.ifttt.sparklemotion;

import android.support.v4.view.ViewPager;

public class AnimationBuilder {

    private final SparkleMotion presenter;
    private final ViewPager viewPager;

    AnimationBuilder(ViewPager viewPager) {
        this.viewPager = viewPager;

        if (SparkleMotionCompat.hasPresenter(viewPager)) {
            presenter = SparkleMotionCompat.getAnimationPresenter(viewPager);
        } else {
            presenter = new SparkleMotion();
        }
    }

    /**
     * Assign animations to SparkleMotion, which will then associate the animations to target Views.
     *
     * @param animation Animation to run.
     */
    public void animate(Animation animation, Page page, Integer id) {
        animation.setPage(page);

        presenter.addAnimation(id, animation);
        if (viewPager == null) {
            throw new NullPointerException("ViewPager cannot be null");
        }

        SparkleMotionCompat.installAnimationPresenter(viewPager, false, presenter);
    }
}
