package com.ifttt.sparklemotiondemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ifttt.sparklemotion.Page;
import com.ifttt.sparklemotion.SparkleMotion;

/**
 * Demo Activity for {@link ZoomOutAnimation}.
 */
public final class ZoomOutViewPagerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.single_view_pager_layout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter());

        ZoomOutAnimation zoomOutAnimation = new ZoomOutAnimation();
        SparkleMotion.newBuilder(viewPager).animate(zoomOutAnimation, Page.allPages(), R.id.pic_img_view);
    }
}
