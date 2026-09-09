import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Storage {
    private static final String FILE_PATH = "data.txt";

    // Save all habits to file
    public static void saveHabits(List<Habit> habits) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Habit h : habits) {
                writer.write("HABIT");
                writer.newLine();
                writer.write(h.getName());
                writer.newLine();
                writer.write(h.getCreationDate().toString());
                writer.newLine();
                writer.write(h.getColor());
                writer.newLine();
                writer.write(String.valueOf(h.getTargetPerWeek()));
                writer.newLine();
                StringBuilder dates = new StringBuilder();
                for (LocalDate d : h.getCompletedDates()) {
                    dates.append(d.toString()).append(",");
                }
                writer.write(dates.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save habits. " + e.getMessage());
        }
    }

    // Load all habits from file — never crashes, even if file is missing/corrupted
    public static List<Habit> loadHabits() {
        List<Habit> habits = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("No saved data found — starting fresh.");
            return habits;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("HABIT")) continue;

                String name = reader.readLine();
                LocalDate creationDate = LocalDate.parse(reader.readLine());
                String color = reader.readLine();
                int targetPerWeek = Integer.parseInt(reader.readLine());
                String datesLine = reader.readLine();

                List<LocalDate> completedDates = new ArrayList<>();
                if (datesLine != null && !datesLine.isEmpty()) {
                    for (String d : datesLine.split(",")) {
                        if (!d.isBlank()) {
                            completedDates.add(LocalDate.parse(d));
                        }
                    }
                }

                habits.add(new Habit(name, creationDate, color, targetPerWeek, completedDates));
            }
        } catch (Exception e) {
            System.out.println("Warning: data.txt was corrupted, some data may be missing. " + e.getMessage());
        }

        return habits;
    }
}