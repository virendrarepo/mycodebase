package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PercentCompleteFilterTest {

    private PercentCompleteFilter filter;

    private static final long REC_START_TIME = getTimeinMillis8AMMorning();
    private static final long REC_END_TIME = getTimeinMillis9AMMorning();
    public static final String RELEVANT_MESSAGE = "recommendation should show relevant message";
    public static final String IRRELEVANT_MESSAGE = "recommendation should show irrelevant message";

    @Before
    public void setUp() {

        filter = new PercentCompleteFilter();
    }


    @Test
    public void shouldShowRelevantRecommendationWhenStartAndEndTimeBeforeTheRecommendationSlot() {
        final long startTime = REC_START_TIME - (20 * 60*1000);
        final long endTime =  REC_START_TIME - (10 * 60*1000);
        boolean isRelevant =  filter.isRelevant(getRecommenndation(), startTime, endTime);
        assertThat(RELEVANT_MESSAGE,true,is(isRelevant));
    }


    @Test
    public void shouldShowRelevantRecommendationWhenStartTimeBeforeAndEndTimeAfterTheREcommendationSlot() {
        final long startTime = REC_START_TIME - (20*60*1000);
        final long endTime =  REC_END_TIME + (20*60*1000);
        boolean isRelevant =  filter.isRelevant(getRecommenndation(), startTime, endTime);
        assertThat(RELEVANT_MESSAGE,true,is(isRelevant));
    }

    @Test
    public void shouldShowIrrelevantMessageWhenStartTimeisAterSixtyPercentOfRunningShow() {
        final long startTime = REC_END_TIME -  (20*60*1000);
        final long endTime =  REC_END_TIME + (5*60*1000);
        boolean isRelevant =  filter.isRelevant(getRecommenndation(), startTime, endTime);
        assertThat(IRRELEVANT_MESSAGE,false, is(isRelevant));
    }


    private Recommendation getRecommenndation() {
        return new Recommendation(UUID.randomUUID().toString(), REC_START_TIME, REC_END_TIME);
    }

    private static Calendar getCalForCurrentDateAndYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        return cal;

    }

    private static long getTimeinMillis8AMMorning() {
        Calendar cal = getCalForCurrentDateAndYear();
        cal.set(Calendar.HOUR, 8);
        return cal.getTimeInMillis();

    }

    private static long getTimeinMillis9AMMorning() {
        Calendar cal = getCalForCurrentDateAndYear();
        cal.set(Calendar.HOUR, 9);
        return cal.getTimeInMillis();

    }
}
