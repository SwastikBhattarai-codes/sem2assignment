import java.util.ArrayList;
import java.util.List;

public class Habit {
    String name;
    List<String> completedDates;

    public Habit(String name) {
        this.name = name;
        this.completedDates = new ArrayList<>();
    }
}