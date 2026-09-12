import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MainWindow {

    private static List<Habit> habits;
    private static DefaultListModel<String> listModel;
    private static JList<String> habitList;
    private static JTextField nameField;
    private static JLabel messageLabel;

    public static void main(String[] args) {
        habits = Storage.loadHabits();

        JFrame frame = new JFrame("My Habit Tracker");
        frame.setSize(500, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // TOP: Add habit
        JPanel topPanel = new JPanel(new FlowLayout());
        nameField = new JTextField(20);
        JButton addButton = new JButton("Add Habit");
        topPanel.add(new JLabel("New Habit:"));
        topPanel.add(nameField);
        topPanel.add(addButton);

        // CENTER: Habit list
        listModel = new DefaultListModel<>();
        habitList = new JList<>(listModel);
        habitList.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
    
        JScrollPane scrollPane = new JScrollPane(habitList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Habits (Select one)"));
        refreshList();

        // BOTTOM: Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton doneButton = new JButton("Mark Done Today");
        JButton deleteButton = new JButton("Delete Habit");
        JButton statsButton = new JButton("View Stats");
        JButton heatmapButton = new JButton("View Heatmap");
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.BLUE);

        bottomPanel.add(doneButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(statsButton);
        bottomPanel.add(heatmapButton);
        bottomPanel.add(messageLabel);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Add habit
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                habits.add(new Habit(name, LocalDate.now(), "#4CAF50", 7));
                Storage.saveHabits(habits);
                refreshList();
                nameField.setText("");
                messageLabel.setText("Added: " + name);
            }
        });

        // Mark done
        doneButton.addActionListener(e -> {
            int selectedIndex = habitList.getSelectedIndex();
            if (selectedIndex != -1) {
                Habit selectedHabit = habits.get(selectedIndex);
                LocalDate today = LocalDate.now();

                if (!selectedHabit.getCompletedDates().contains(today)) {
                    selectedHabit.getCompletedDates().add(today);
                    Storage.saveHabits(habits);
                    refreshList();
                    habitList.setSelectedIndex(selectedIndex);
                    messageLabel.setText("Done! Streak: " + selectedHabit.getStreak() + " days");
                } else {
                    messageLabel.setText("Already done today!");
                }
            } else {
                messageLabel.setText("Please select a habit first!");
            }
        });

        // Delete habit
        deleteButton.addActionListener(e -> {
            int selectedIndex = habitList.getSelectedIndex();
            if (selectedIndex != -1) {
                String name = habits.get(selectedIndex).getName();
                habits.remove(selectedIndex);
                Storage.saveHabits(habits);
                refreshList();
                messageLabel.setText("Deleted: " + name);
            } else {
                messageLabel.setText("Please select a habit to delete!");
            }
        });

        // View stats
        statsButton.addActionListener(e -> {
            int selectedIndex = habitList.getSelectedIndex();
            if (selectedIndex != -1) {
                Habit h = habits.get(selectedIndex);
                int longest = StatsCalculator.getLongestStreak(h);
                double rate = StatsCalculator.getCompletionRateLast30Days(h);
                String weekly = StatsCalculator.getWeeklyProgress(h);

                String statsMessage =
                        "Habit: " + h.getName() + "\n" +
                        "Current Streak: " + h.getStreak() + " days\n" +
                        "Longest Streak: " + longest + " days\n" +
                        "Last 30 Days Rate: " + String.format("%.1f", rate) + "%\n" +
                        "This Week: " + weekly;

                JOptionPane.showMessageDialog(frame, statsMessage, "Habit Stats", JOptionPane.INFORMATION_MESSAGE);
            } else {
                messageLabel.setText("Please select a habit to view stats!");
            }
        });

        // View heatmap
        heatmapButton.addActionListener(e -> {
            int selectedIndex = habitList.getSelectedIndex();
            if (selectedIndex != -1) {
                Habit selectedHabit = habits.get(selectedIndex);
                JFrame heatmapFrame = new JFrame("Heatmap: " + selectedHabit.getName());
                heatmapFrame.setSize(950, 250);
                heatmapFrame.add(new HeatmapPanel(selectedHabit));
                heatmapFrame.setLocationRelativeTo(frame);
                heatmapFrame.setVisible(true);
            } else {
                messageLabel.setText("Please select a habit to view heatmap!");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void refreshList() {
        listModel.clear();
        for (Habit h : habits) {
            listModel.addElement("🔥 " + h.getStreak() + " days  |  " + h.getName());
        }
    }
}