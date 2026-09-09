import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MainWindow {
    
    // Our data
    private static List<Habit> habits;
    
    // UI Components
    private static DefaultListModel<String> listModel;
    private static JList<String> habitList;
    private static JTextField nameField;
    private static JLabel messageLabel;

    public static void main(String[] args) {
        // Load saved habits
        habits = Storage.load();

        // Create the main window
        JFrame frame = new JFrame("My Habit Tracker");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // 1. TOP PANEL (Add Habit)
        JPanel topPanel = new JPanel(new FlowLayout());
        nameField = new JTextField(20);
        JButton addButton = new JButton("Add Habit");
        topPanel.add(new JLabel("New Habit:"));
        topPanel.add(nameField);
        topPanel.add(addButton);

        // 2. CENTER PANEL (List of Habits)
        listModel = new DefaultListModel<>();
        habitList = new JList<>(listModel);
        habitList.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(habitList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Habits (Select one)"));
        
        refreshList(); // Load habits into the UI

        // 3. BOTTOM PANEL (Actions)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton doneButton = new JButton("Mark Done Today");
        JButton deleteButton = new JButton("Delete Habit");
        JButton statsButton = new JButton("View Stats");
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.BLUE);
        
        bottomPanel.add(doneButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(statsButton);
        bottomPanel.add(messageLabel);

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // --- BUTTON ACTIONS ---

        // Add Button
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                habits.add(new Habit(name));
                Storage.save(habits);
                refreshList();
                nameField.setText("");
                messageLabel.setText("Added: " + name);
            }
        });

        // Mark Done Button
        doneButton.addActionListener(e -> {
            int selectedIndex = habitList.getSelectedIndex();
            if (selectedIndex != -1) {
                Habit selectedHabit = habits.get(selectedIndex);
                String today = LocalDate.now().toString();
                
                if (!selectedHabit.completedDates.contains(today)) {
                    selectedHabit.completedDates.add(today);
                    Storage.save(habits);
                    
                    // THE FIX: Refresh the list so the fire emoji updates!
                    refreshList(); 
                    
                    // Keep the habit selected after refreshing
                    habitList.setSelectedIndex(selectedIndex); 
                    
                    messageLabel.setText("✅ " + selectedHabit.name + " done! Streak: " + selectedHabit.getStreak() + " days");
                } else {
                    messageLabel.setText("Already done today!");
                }
            } else {
                messageLabel.setText("Please select a habit first!");
            }
        });

        // Delete Button
        deleteButton.addActionListener(e -> {
            int selectedIndex = habitList.getSelectedIndex();
            if (selectedIndex != -1) {
                String name = habits.get(selectedIndex).name;
                habits.remove(selectedIndex);
                Storage.save(habits);
                refreshList();
                messageLabel.setText("Deleted: " + name);
            } else {
                messageLabel.setText("Please select a habit to delete!");
            }
        });
                // View Stats Button
        statsButton.addActionListener(e -> {
            int selectedIndex = habitList.getSelectedIndex();

            if (selectedIndex != -1) {
                Habit selectedHabit = habits.get(selectedIndex);
                String stats = StatsCalculator.getStatsText(selectedHabit);

                JOptionPane.showMessageDialog(frame, stats, "Stats", JOptionPane.INFORMATION_MESSAGE);
            } else {
                messageLabel.setText("Please select a habit to view stats!");
            }
        });

        // Show window
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
    }

    // Helper method to update the visual list
    private static void refreshList() {
        listModel.clear();
        for (Habit h : habits) {
            listModel.addElement("🔥 " + h.getStreak() + " days  |  " + h.name);
        }
    }
}