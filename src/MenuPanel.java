import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel{
    // Runnable = "potongan aksi" yang dikirim dari App
    public MenuPanel(Runnable onStart, Runnable onExit) {
        setPreferredSize(new Dimension(360, 640));
        setBackground(Color.cyan);
        setLayout(new GridBagLayout());

        JLabel title = new JLabel("FLAPPY BIRD");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);

        JButton startButton = new JButton("Mulai Permainan");
        JButton exitButton = new JButton("Keluar");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));

        startButton.addActionListener(e -> onStart.run());
        exitButton.addActionListener(e -> onExit.run());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; add(title, gbc);
        gbc.gridy = 1; add(startButton, gbc);
        gbc.gridy = 2; add(exitButton, gbc);
    }
}
