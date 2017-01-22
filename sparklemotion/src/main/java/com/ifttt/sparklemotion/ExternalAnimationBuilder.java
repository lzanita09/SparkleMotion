package com.ifttt.sparklemotion;

import android.support.v4.view.ViewPager;
import android.view.View;

public class ExternalAnimationBuilder extends AnimationBuilder{
    private final SparkleViewPagerLayout viewPagerLayout;
    private final SparkleMotion presenter;

    ExternalAnimationBuilder(SparkleViewPagerLayout layout) {
        super(layout.getViewPager());
        this.viewPagerLayout = layout;

        if (SparkleMotionCompat.hasPresenter(layout.getViewPager())) {
            presenter = SparkleMotionCompat.getAnimationPresenter(layout.getViewPager());
        } else {
            presenter = new SparkleMotion();
        }
    }

    public void animate(Animation animation, Page page, View view) {
        if (viewPagerLayout == null) {
            throw new IllegalStateException("To animate external View, SparkleViewPagerLayout must be used.");
        }

        animation.setPage(page);

        presenter.addAnimation(view, animation);

        ViewPager viewPager = viewPagerLayout.getViewPager();

        if (viewPager == null) {
            throw new NullPointerException("ViewPager is not presented in SparkleViewPagerLayout.");
        }

        SparkleMotionCompat.installAnimationPresenter(viewPager, false, presenter);
    }
}
