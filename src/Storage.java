import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    private static final String FILE_PATH = "habits.csv";

    // Save all habits to CSV
    public static void saveHabits(List<Habit> habits) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_PATH))) {

            // CSV header
            writer.write("name,creationDate,color,targetPerWeek,completedDates");
            writer.newLine();

            // Save each habit
            for (Habit habit : habits) {

                StringBuilder dates = new StringBuilder();

                // Join completed dates using ;
                for (LocalDate date : habit.getCompletedDates()) {

                    if (dates.length() > 0) {
                        dates.append(";");
                    }

                    dates.append(date.toString());
                }

                writer.write(
                        habit.getName() + "," +
                        habit.getCreationDate() + "," +
                        habit.getColor() + "," +
                        habit.getTargetPerWeek() + "," +
                        dates
                );

                writer.newLine();
            }

            System.out.println("Habits saved successfully!");

        } catch (IOException e) {

            System.out.println(
                    "Warning: Could not save habits. "
                            + e.getMessage()
            );
        }
    }


    // Load all habits from CSV
    public static List<Habit> loadHabits() {

        List<Habit> habits = new ArrayList<>();

        File file = new File(FILE_PATH);

        // If the file does not exist, start fresh
        if (!file.exists()) {

            System.out.println(
                    "No saved data found. Starting fresh."
            );

            return habits;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            // Skip the header
            reader.readLine();

            // Read each habit
            while ((line = reader.readLine()) != null) {

                try {

                    String[] data = line.split(",", -1);

                    // Check if the row has all required fields
                    if (data.length != 5) {

                        System.out.println(
                                "Skipping corrupted row: " + line
                        );

                        continue;
                    }

                    String name = data[0];

                    LocalDate creationDate =
                            LocalDate.parse(data[1]);

                    String color = data[2];

                    int targetPerWeek =
                            Integer.parseInt(data[3]);

                    List<LocalDate> completedDates =
                            new ArrayList<>();

                    // Read completed dates
                    if (!data[4].isBlank()) {

                        String[] dates =
                                data[4].split(";");

                        for (String date : dates) {

                            completedDates.add(
                                    LocalDate.parse(date)
                            );
                        }
                    }

                    Habit habit = new Habit(
                            name,
                            creationDate,
                            color,
                            targetPerWeek,
                            completedDates
                    );

                    habits.add(habit);

                } catch (Exception e) {

                    // Skip only the bad row instead of crashing
                    System.out.println(
                            "Skipping corrupted row: " + line
                    );
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Warning: Could not load habits. "
                            + e.getMessage()
            );
        }

        return habits;
    }
}