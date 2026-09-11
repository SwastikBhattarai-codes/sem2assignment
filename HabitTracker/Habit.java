import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Habit {
    private String name;
    private LocalDate creationDate;
    private String color;
    private int targetPerWeek;
    private List<LocalDate> completedDates;

    // Constructor for NEW habits (when adding)
    public Habit(String name, LocalDate creationDate, String color, int targetPerWeek) {
        this.name = name;
        this.creationDate = creationDate;
        this.color = color;
        this.targetPerWeek = targetPerWeek;
        this.completedDates = new ArrayList<>();
    }

    // Constructor for LOADED habits (from CSV)
    public Habit(String name, LocalDate creationDate, String color, int targetPerWeek, List<LocalDate> completedDates) {
        this.name = name;
        this.creationDate = creationDate;
        this.color = color;
        this.targetPerWeek = targetPerWeek;
        this.completedDates = completedDates;
    }

    // Getters (Required by Storage.java)
    public String getName() { return name; }
    public LocalDate getCreationDate() { return creationDate; }
    public String getColor() { return color; }
    public int getTargetPerWeek() { return targetPerWeek; }
    public List<LocalDate> getCompletedDates() { return completedDates; }

    // Streak calculation updated to work with LocalDate directly
    public int getStreak() {
        int streak = 0;
        LocalDate date = LocalDate.now();
        while (completedDates.contains(date)) {
            streak++;
            date = date.minusDays(1);
        }
        return streak;
    }
}