import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class HabitTracker {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Habit> habits = Storage.load();

        while (true) {
            System.out.println("\n===== HABIT TRACKER =====");
            System.out.println("1. Add habit");
            System.out.println("2. Mark habit done today");
            System.out.println("3. Show habits and streaks");
            System.out.println("4. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Habit name: ");
                String name = scanner.nextLine();
                habits.add(new Habit(name));
                Storage.save(habits);
                System.out.println("Habit added!");

            } else if (choice == 2) {
                showHabits(habits);
                System.out.print("Habit number to mark done: ");
                int index = scanner.nextInt() - 1;

                if (index >= 0 && index < habits.size()) {
                    String today = LocalDate.now().toString();
                    Habit habit = habits.get(index);

                    if (!habit.completedDates.contains(today)) {
                        habit.completedDates.add(today);
                        Storage.save(habits);
                        System.out.println("Marked done for " + today);
                    } else {
                        System.out.println("Already done today!");
                    }
                } else {
                    System.out.println("Invalid number.");
                }

            } else if (choice == 3) {
                System.out.println("\n--- Your Habits ---");
                if (habits.isEmpty()) {
                    System.out.println("No habits yet. Add one first!");
                } else {
                    for (int i = 0; i < habits.size(); i++) {
                        System.out.println((i + 1) + ". " + habits.get(i).name 
                            + " | Streak: " + getStreak(habits.get(i)) + " days");
                    }
                }

            } else if (choice == 4) {
                Storage.save(habits);
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    static void showHabits(List<Habit> habits) {
        if (habits.isEmpty()) {
            System.out.println("No habits yet.");
            return;
        }
        for (int i = 0; i < habits.size(); i++) {
            System.out.println((i + 1) + ". " + habits.get(i).name);
        }
    }

    static int getStreak(Habit habit) {
        int streak = 0;
        LocalDate date = LocalDate.now();

        while (habit.completedDates.contains(date.toString())) {
            streak++;
            date = date.minusDays(1);
        }

        return streak;
    }
}