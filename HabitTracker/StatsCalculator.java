import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatsCalculator {

    // 1. Calculate the longest streak ever
    public static int getLongestStreak(Habit habit) {
        List<LocalDate> dates = habit.getCompletedDates();
        if (dates.isEmpty()) return 0;

        // Sort the dates from oldest to newest
        List<LocalDate> sortedDates = new ArrayList<>(dates);
        Collections.sort(sortedDates);

        int longest = 1;
        int current = 1;

        for (int i = 1; i < sortedDates.size(); i++) {
            // If this date is exactly 1 day after the previous date
            if (sortedDates.get(i).equals(sortedDates.get(i - 1).plusDays(1))) {
                current++;
                if (current > longest) {
                    longest = current;
                }
            } else if (!sortedDates.get(i).equals(sortedDates.get(i - 1))) {
                // If it's not the same day and not the next day, reset streak
                current = 1;
            }
        }
        return longest;
    }

    // 2. Calculate completion rate for the last 30 days (%)
    public static double getCompletionRateLast30Days(Habit habit) {
        List<LocalDate> dates = habit.getCompletedDates();
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);

        long count = 0;
        for (LocalDate date : dates) {
            if (!date.isBefore(thirtyDaysAgo) && !date.isAfter(today)) {
                count++;
            }
        }

        return (count / 30.0) * 100.0; 
    }

    // 3. Calculate weekly progress based on targetPerWeek
    public static String getWeeklyProgress(Habit habit) {
        List<LocalDate> dates = habit.getCompletedDates();
        LocalDate today = LocalDate.now();
        
        // Find the start of the current week (Monday)
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);

        long count = 0;
        for (LocalDate date : dates) {
            if (!date.isBefore(startOfWeek) && !date.isAfter(today)) {
                count++;
            }
        }

        return count + " / " + habit.getTargetPerWeek();
    }
}