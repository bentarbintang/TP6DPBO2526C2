import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    int width = 360;
    int height = 640;

    private Logic logic;
    private JLabel scoreLabel;

    public View(Logic logic){
        this.logic = logic;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.cyan);

        //tambahkan fokus dan key listener
        //untuk menerima input

        setFocusable(true);
        addKeyListener(logic);

        setLayout(null);
        scoreLabel = new JLabel("0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 40));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(0, 20, width, 50);
        add(scoreLabel);
    }

    public void setScore(int score) {
        scoreLabel.setText(String.valueOf(score));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // background (paling belakang)
        g.drawImage(logic.getBackgroundImage(), 0, 0, width, height, null);

        //burungnya
        Player player = logic.getPlayer();
        if(player != null){
            g.drawImage(player.getimage(), player.getX(), player.getY(),
                    player.getWidth(), player.getHeight(), null);
        }

        //pipa
        ArrayList<Pipe> pipes = logic.getPipes();
        if(pipes != null){
            for(int i = 0; i < pipes.size(); i++){
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
            }
        }


        // layar game over
        if (logic.isGameOver()) {
            g.setColor(new Color(0, 0, 0, 150));      // lapisan gelap transparan
            g.fillRect(0, 0, width, height);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            drawCentered(g, "GAME OVER", 250);

            g.setFont(new Font("Arial", Font.PLAIN, 22));
            drawCentered(g, "Skor: " + logic.getScore(), 300);
            drawCentered(g, "High Score: " + logic.getHighScore(), 335);
            drawCentered(g, "Tekan R untuk restart", 390);
        }
    }

    private void drawCentered(Graphics g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}
