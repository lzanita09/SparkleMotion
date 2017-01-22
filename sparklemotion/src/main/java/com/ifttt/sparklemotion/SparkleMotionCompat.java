package com.ifttt.sparklemotion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Helper class for installing {@link SparkleMotion} into a {@link ViewPager}, so
 * that any ViewPager can use SparkleMotion animations.
 * <p/>
 * The {@link SparkleMotion} instance will be referenced by {@link View#setTag(int,
 * Object)} within the ViewPager, and a {@link android.support.v4.view.ViewPager.PageTransformer}
 * and a {@link android.support.v4.view.ViewPager.OnPageChangeListener} will be set as well
 * to provide functionality of SparkleMotion.
 * <p/>
 * Note: Once a SparkleMotion is installed, if you would like to provide another
 * PageTransformer, you need to use
 * {@link #setPageTransformer(ViewPager, boolean, ViewPager.PageTransformer)} method, so
 * that the presenter is preserved.
 */
public final class SparkleMotionCompat {

    private SparkleMotionCompat() {
        throw new AssertionError("No instance");
    }

    /**
     * Given a ViewPager and a {@link SparkleMotion}, sets a PageTransformer and a
     * OnPageChangeListener that provides functionality of the presenter.
     * The presenter will be referenced as a tag of the View.
     *
     * @param viewPager ViewPager instance.
     * @param reverseDrawingOrder Whether the ViewPager should reverse it child Views' drawing
     * order.
     * @param presenter SparkleMotion instance to be installed.
     * @see {@link ViewPager#setPageTransformer(boolean, ViewPager.PageTransformer)}
     */
    static void installAnimationPresenter(@NonNull final ViewPager viewPager, boolean reverseDrawingOrder,
            final SparkleMotion presenter) {
        Object tagObject = viewPager.getTag(R.id.presenter_id);
        if (tagObject != null && tagObject == presenter) {
            // If the presenter is the same as the one already in the ViewPager, return.
            return;
        }

        // Set PageTransformer
        ViewPager.PageTransformer transformer = new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int pageWidth = page.getWidth();
                float offset = pageWidth * -position;

                presenter.presentAnimations(page, position, offset);
            }
        };

        // Set OnPageChangeListener
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Animate any Decor animations.
                presenter.presentDecorAnimations(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                final int layerType =
                        state == ViewPager.SCROLL_STATE_IDLE ? View.LAYER_TYPE_NONE : View.LAYER_TYPE_HARDWARE;

                final int animatedViewsCount = presenter.getAnimatedViews().size();
                for (int id : presenter.getAnimatedViews()) {
                    View child = viewPager.findViewById(id);
                    if (child != null) {
                        child.setLayerType(layerType, null);
                    }
                }

                if (animatedViewsCount > 0) {
                    // Set layer type back to none when there is ViewPager animations from Sparkle Motion.
                    final int viewPagerChildCount = viewPager.getChildCount();
                    for (int i = 0; i < viewPagerChildCount; i++) {
                        View child = viewPager.getChildAt(i);
                        child.setLayerType(View.LAYER_TYPE_NONE, null);
                    }
                }

            }
        });

        viewPager.setPageTransformer(reverseDrawingOrder, transformer);

        viewPager.setTag(R.id.presenter_id, presenter);
    }

    /**
     * Convenient method for installing {@link SparkleMotion}. A new
     * SparkleMotion will be created and attached to the ViewPager.
     *
     * @param viewPager ViewPager instance.
     * @param reverseDrawingOrder Whether the ViewPager should reverse it child Views' drawing
     * order.
     * @see {@link #installAnimationPresenter(ViewPager, boolean, SparkleMotion)}
     * @see {@link #installAnimationPresenter(ViewPager)}
     */
    static void installAnimationPresenter(@NonNull ViewPager viewPager, boolean reverseDrawingOrder) {
        final SparkleMotion presenter = new SparkleMotion();
        installAnimationPresenter(viewPager, reverseDrawingOrder, presenter);
    }

    /**
     * Convenient method for installing {@link SparkleMotion} without reversing
     * ViewPager's child Views drawing order.
     * A new SparkleMotion will be created and attached to the ViewPager.
     *
     * @param viewPager ViewPager instance.
     * @see {@link #installAnimationPresenter(ViewPager, boolean, SparkleMotion)}
     * @see {@link #installAnimationPresenter(ViewPager, boolean)}
     */
    static void installAnimationPresenter(@NonNull ViewPager viewPager) {
        installAnimationPresenter(viewPager, false);
    }

    /**
     * Sets a {@link android.support.v4.view.ViewPager.PageTransformer} to the given ViewPager.
     * Using this method to set PageTransformer is required for the ViewPagers that already have
     * {@link SparkleMotion} installed.
     *
     * @param viewPager ViewPager instance.
     * @param reversDrawingOrder Whether the ViewPager should reverse it child Views' drawing
     * order.
     * @param transformer PageTransformer instance.
     * @see {@link ViewPager#setPageTransformer(boolean, ViewPager.PageTransformer)}
     * @see {@link android.support.v4.view.ViewPager.PageTransformer}
     */
    public static void setPageTransformer(@NonNull ViewPager viewPager, boolean reversDrawingOrder,
            @Nullable final ViewPager.PageTransformer transformer) {
        Object tagObject = viewPager.getTag(R.id.presenter_id);
        if (tagObject == null || !(tagObject instanceof SparkleMotion)) {
            viewPager.setPageTransformer(reversDrawingOrder, transformer);
            return;
        }

        final SparkleMotion presenter = (SparkleMotion) tagObject;
        ViewPager.PageTransformer transformerWrapper = new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int pageWidth = page.getWidth();
                float offset = pageWidth * -position;

                presenter.presentAnimations(page, position, offset);
                if (transformer != null) {
                    transformer.transformPage(page, position);
                }
            }
        };

        viewPager.setPageTransformer(reversDrawingOrder, transformerWrapper);
    }

    /**
     * Gets the {@link SparkleMotion} instance attached to the given ViewPager.
     *
     * @param viewPager ViewPager instance.
     * @return SparkleMotion instance if set, or null.
     */
    static SparkleMotion getAnimationPresenter(ViewPager viewPager) {
        if (viewPager == null) {
            return null;
        }

        Object tagObject = viewPager.getTag(R.id.presenter_id);
        if (tagObject == null || !(tagObject instanceof SparkleMotion)) {
            return null;
        }

        return (SparkleMotion) tagObject;
    }

    /**
     * Convenient method to check whether {@link SparkleMotion} is attached to the
     * given ViewPager.
     *
     * @param viewPager ViewPager instance.
     * @return True if there is a SparkleMotion attached, false otherwise.
     */
    static boolean hasPresenter(ViewPager viewPager) {
        return getAnimationPresenter(viewPager) != null;
    }
}
