package com.ifttt.sparklemotion;

import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Animation driver, used to store all {@link Animation} assigned to it and run animations given the current
 * circumstance (e.g current page, View visibility). For ViewPager animations,
 * {@link #presentAnimations(View, float, float)} will be called for every frame the child View is scrolled. For
 * Decor animations, {@link #presentDecorAnimations(int, float)} will be called for every frame the ViewPager is
 * scrolled.
 */
public class SparkleMotion {
    /**
     * A SimpleArrayMap that saves all animations newBuilder the target View's ID as key.
     */
    private SimpleArrayMap<Integer, ArrayList<Animation>> animations;

    /**
     * A SimpleArrayMap that saves all animations newBuilder the target
     * {@link View} as key.
     */
    private SimpleArrayMap<View, ArrayList<Animation>> decorAnimations;

    /**
     * An ArrayList that saves the ids of the Views being animated newBuilder Sparkle Motion.
     */
    private ArrayList<Integer> animatedViews;

    /**
     * Start constructing a {@link SparkleMotion} builder newBuilder a {@link ViewPager} instance. Animations
     * assigned
     * to this builder will be assigned to the ViewPager.
     *
     * @param viewPager Target ViewPager.
     * @return this instance to chain functions.
     */
    public static AnimationBuilder newBuilder(@NonNull ViewPager viewPager) {
        return new AnimationBuilder(viewPager);
    }

    /**
     * Start constructing a {@link SparkleMotion} builder newBuilder a {@link SparkleViewPagerLayout}
     * instance. Animations
     * assigned to this builder will be assigned to the ViewPager.
     *
     * @param viewPagerLayout TargetViewPagerLayout.
     * @return this instance to chain functions.
     */
    public static ExternalAnimationBuilder newBuilder(@NonNull SparkleViewPagerLayout viewPagerLayout) {
        return new ExternalAnimationBuilder(viewPagerLayout);
    }

    SparkleMotion() {
        animations = new SimpleArrayMap<>(3);
        decorAnimations = new SimpleArrayMap<>(3);
        animatedViews = new ArrayList<>(3);
    }

    /**
     * Add animations to the target View. The View's id is used as key.
     *
     * @param id         Id of the target View.
     * @param animation Animations to be associated to this View.
     */
    void addAnimation(int id, Animation animation) {
        if (this.animations.get(id) == null) {
            this.animations.put(id, new ArrayList<Animation>());
            animatedViews.add(id);
        }

        ArrayList<Animation> anims = this.animations.get(id);
        anims.add(animation);
    }

    /**
     * Add animations to the target {@link View}.
     *
     * @param view      Target Decor.
     * @param animations Animations to be associated to this Decor.
     */
    void addAnimation(View view, Animation... animations) {
        if (decorAnimations.get(view) == null) {
            decorAnimations.put(view, new ArrayList<Animation>(animations.length));
        }

        ArrayList<Animation> anims = decorAnimations.get(view);
        if (anims != null) {
            Collections.addAll(anims, animations);
        }
    }

    /**
     * Run the animations based on the View animations saved within the presenter and the offset of
     * the scrolling.
     *
     * @param parent        Current page View of the ViewPager.
     * @param offset        Scrolling offset of the ViewPager.
     * @param offsetInPixel Scrolling offset in pixels based on the page View.
     */
    void presentAnimations(View parent, float offset, float offsetInPixel) {
        int animMapSize = animations.size();

        // Animate all in-page animations.
        for (int i = 0; i < animMapSize; i++) {
            int key = animations.keyAt(i);
            ArrayList<Animation> animations = this.animations.get(key);

            int animListSize = animations.size();
            for (int j = 0; j < animListSize; j++) {
                Animation animation = animations.get(j);

                final View viewToAnimate;

                if (key == parent.getId()) {
                    viewToAnimate = parent;
                } else {
                    viewToAnimate = parent.findViewById(key);
                }

                if (animation == null || viewToAnimate == null) {
                    continue;
                }

                animation.animate(viewToAnimate, offset, offsetInPixel);
            }
        }
    }

    /**
     * Run the animations based on the Decor animations saved within the presenter and the offset
     * of the scrolling.
     *
     * @param position Position of the current page.
     * @param offset   Offset of the ViewPager scrolling.
     */
    void presentDecorAnimations(int position, float offset) {
        // Animate all decor or other View animations.
        int animMapSize = decorAnimations.size();
        for (int i = 0; i < animMapSize; i++) {
            View view = decorAnimations.keyAt(i);
            ArrayList<Animation> animations = decorAnimations.get(view);

            int animListSize = animations.size();
            for (int j = 0; j < animListSize; j++) {
                Animation animation = animations.get(j);
                if (animation == null) {
                    continue;
                }

                if (!shouldAnimate(animation.getPage(), position)) {
                    continue;
                }

                animation.animate(view, offset, 0);
            }
        }
    }

    /**
     * @return A List of ids that Sparkle Motion animates within the ViewPager.
     */
    List<Integer> getAnimatedViews() {
        return animatedViews;
    }

    /**
     * Check the current page newBuilder {@link SparkleMotion} and see if it is within
     * {@link Page}.
     *
     * @param currentPage Current page in ViewPager where the scroll starts.
     * @return True if the animation should run, false otherwise.
     */
    static boolean shouldAnimate(Page page, int currentPage) {
        return (page.start == page.end && page.start == Page.ALL_PAGES)
                || page.start <= currentPage && page.end >= currentPage;
    }
}
