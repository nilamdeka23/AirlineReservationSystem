package edu.sjsu.cmpe275.utils;


import org.joda.time.Interval;

import java.util.List;

/**
 * reference URL: stackoverflow.com/questions/24497809/compare-intervals-jodatime-in-a-list-for-overlap
 */
public class DateTimeUtil {

    /**
     * Is overlapping boolean.
     *
     * @param sortedIntervals the sorted intervals
     * @return the boolean
     */
    public static boolean isOverlapping(List<Interval> sortedIntervals) {
        for (int i = 0, n = sortedIntervals.size(); i < n - 1; i++) {
            if (sortedIntervals.get(i).overlaps(sortedIntervals.get(i + 1))) {
                return true;
            }
        }

        return false;
    }
}
