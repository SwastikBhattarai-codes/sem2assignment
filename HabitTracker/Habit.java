import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Habit {
    public String name;
    public List<String> completedDates;

    public Habit(String name) {
        this.name = name;
        this.completedDates = new ArrayList<>();
    }

    // New method to calculate the streak!
    public int getStreak() {
        int streak = 0;
        LocalDate date = LocalDate.now();

        while (completedDates.contains(date.toString())) {
            streak++;
            date = date.minusDays(1);
        }

        return streak;
    }
}