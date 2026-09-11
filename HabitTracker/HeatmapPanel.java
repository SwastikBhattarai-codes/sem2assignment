import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.LocalDate;

public class HeatmapPanel extends JPanel {

    private Habit habit;

    public HeatmapPanel(Habit habit) {
        this.habit = habit;
        setPreferredSize(new java.awt.Dimension(900, 180));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        LocalDate today = LocalDate.now();

        int squareSize = 12;
        int gap = 3;

        // Draw 52 weeks × 7 days
        for (int week = 0; week < 52; week++) {

            for (int day = 0; day < 7; day++) {

                LocalDate date = today
                        .minusWeeks(51 - week)
                        .with(java.time.DayOfWeek.MONDAY)
                        .plusDays(day);

                int x = week * (squareSize + gap);
                int y = day * (squareSize + gap);

                if (habit.getCompletedDates().contains(date)) {
                    g2.setColor(new Color(76, 175, 80)); // completed
                } else {
                    g2.setColor(new Color(220, 220, 220)); // missed
                }

                g2.fillRect(x, y, squareSize, squareSize);
            }
        }
    }
}