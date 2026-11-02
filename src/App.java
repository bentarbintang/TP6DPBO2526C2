import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args){
        JFrame frame = new JFrame ("Flappybird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        CardLayout cards = new CardLayout();
        JPanel container = new JPanel(cards);

        Logic logika = new Logic();
        View tampilan = new View(logika);
        logika.setView(tampilan);

        MenuPanel menu = new MenuPanel(
                () -> {                                   // tombol Mulai
                    cards.show(container, "game");
                    logika.startGame();
                    tampilan.requestFocusInWindow();      // supaya keyboard diterima
                },
                () -> System.exit(0)                      // tombol Keluar
        );

        container.add(menu, "menu");
        container.add(tampilan, "game");

        frame.add(container);
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
