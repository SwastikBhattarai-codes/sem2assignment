import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Habit {
    private String name;
    private LocalDate creationDate;
    private String color;          // hex code, e.g. "#4CAF50"
    private int targetPerWeek;
    private List<LocalDate> completedDates;

    // Constructor for creating a brand-new habit
    public Habit(String name, String color, int targetPerWeek) {
        this.name = name;
        this.creationDate = LocalDate.now();
        this.color = color;
        this.targetPerWeek = targetPerWeek;
        this.completedDates = new ArrayList<>();
    }

    // Constructor for rebuilding a habit when loading from storage
    public Habit(String name, LocalDate creationDate, String color,
                 int targetPerWeek, List<LocalDate> completedDates) {
        this.name = name;
        this.creationDate = creationDate;
        this.color = color;
        this.targetPerWeek = targetPerWeek;
        this.completedDates = completedDates;
    }

    public void markDoneToday() {
        LocalDate today = LocalDate.now();
        if (!completedDates.contains(today)) {
            completedDates.add(today);
        }
    }

    // Getters (Person 2, 3, 4 will need these to read your data)
    public String getName() { return name; }
    public LocalDate getCreationDate() { return creationDate; }
    public String getColor() { return color; }
    public int getTargetPerWeek() { return targetPerWeek; }
    public List<LocalDate> getCompletedDates() { return completedDates; }

    // Setters, in case someone needs to edit a habit
    public void setColor(String color) { this.color = color; }
    public void setTargetPerWeek(int targetPerWeek) { this.targetPerWeek = targetPerWeek; }
}