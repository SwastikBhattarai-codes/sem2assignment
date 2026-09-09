import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

public class StatsCalculator {

    public static int getLongestStreak(Habit habit) {
        TreeSet<LocalDate> dates = new TreeSet<>();

        for (String date : habit.completedDates) {
            dates.add(LocalDate.parse(date));
        }

        int longest = 0;
        int current = 0;
        LocalDate previous = null;

        for (LocalDate date : dates) {
            if (previous != null && date.equals(previous.plusDays(1))) {
                current++;
            } else {
                current = 1;
            }

            if (current > longest) {
                longest = current;
            }

            previous = date;
        }

        return longest;
    }

    public static int getTotalCompletedDays(Habit habit) {
        TreeSet<LocalDate> dates = new TreeSet<>();

        for (String date : habit.completedDates) {
            dates.add(LocalDate.parse(date));
        }

        return dates.size();
    }

    public static int getCompletionRate(Habit habit) {
        TreeSet<LocalDate> dates = new TreeSet<>();

        for (String date : habit.completedDates) {
            dates.add(LocalDate.parse(date));
        }

        if (dates.isEmpty()) {
            return 0;
        }

        LocalDate firstDate = dates.first();
        LocalDate today = LocalDate.now();

        long totalDays = ChronoUnit.DAYS.between(firstDate, today) + 1;

        if (totalDays <= 0) {
            totalDays = 1;
        }

        long completedDays = dates.size();

        return (int) ((completedDays * 100) / totalDays);
    }

    public static int getThisWeekCount(Habit habit) {
        int count = 0;
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            String date = today.minusDays(i).toString();

            if (habit.completedDates.contains(date)) {
                count++;
            }
        }

        return count;
    }

    public static String getStatsText(Habit habit) {
        String text = "";

        text += "Habit: " + habit.name + "\n";
        text += "Current streak: " + habit.getStreak() + " days\n";
        text += "Longest streak: " + getLongestStreak(habit) + " days\n";
        text += "Total completed days: " + getTotalCompletedDays(habit) + "\n";
        text += "Completion rate: " + getCompletionRate(habit) + "%\n";
        text += "Completed in last 7 days: " + getThisWeekCount(habit) + "/7";

        return text;
    }
}