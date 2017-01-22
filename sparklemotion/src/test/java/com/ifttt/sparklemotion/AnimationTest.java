package com.ifttt.sparklemotion;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

@SmallTest
public final class AnimationTest {

    @Test
    public void testShouldAnimate() throws Exception {

        assertEquals(true, SparkleMotion.shouldAnimate(Page.singlePage(0), 0));
        assertEquals(false, SparkleMotion.shouldAnimate(Page.singlePage(0), 2));
        assertEquals(false, SparkleMotion.shouldAnimate(Page.pageRange(0, 1), 2));
    }
}
