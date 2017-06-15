package edu.sjsu.cmpe275.utils;

import org.joda.time.Interval;

import java.util.Comparator;

/**
 * reference URL: stackoverflow.com/questions/16986888/sort-joda-time-interval‌​s
 */
public class IntervalStartComparator implements Comparator<Interval> {
    @Override
    public int compare(Interval x, Interval y) {
        return x.getStart().compareTo(y.getStart());
    }
}