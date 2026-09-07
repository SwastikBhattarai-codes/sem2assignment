import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    static void save(List<Habit> habits) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data.txt"))) {
            for (Habit habit : habits) {
                writer.println("HABIT:" + habit.name);
                for (String date : habit.completedDates) {
                    writer.println("DATE:" + date);
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving data");
        }
    }

    static List<Habit> load() {
        List<Habit> habits = new ArrayList<>();
        File file = new File("data.txt");

        if (!file.exists()) {
            return habits;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Habit current = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("HABIT:")) {
                    current = new Habit(line.substring(6));
                    habits.add(current);
                } else if (line.startsWith("DATE:") && current != null) {
                    current.completedDates.add(line.substring(5));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data");
        }

        return habits;
    }
}