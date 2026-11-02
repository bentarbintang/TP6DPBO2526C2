import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.sound.sampled.Clip;

public class Logic implements ActionListener, KeyListener{
    int frameWidth = 360;
    int frameHeight = 640;

    int playerStartPosX = frameWidth / 2;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    int pipeVelocityX = -2;

    View view;
    Image birdImage;
    Player player;

    //list pipa dan gambar
    Image lowerPipeImage;
    Image upperPipeImage;
    ArrayList<Pipe> pipes;

    //atribut baru
    boolean gameOver = false;
    int score = 0;
    int highScore = 0;
    Image backgroundImage;
    Clip jumpSound, scoreSound, hitSound;
    boolean started = false;

    public void startGame() {
        resetGame();
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public Logic(){
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);

        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();
        pipes = new ArrayList<Pipe>();

        jumpSound = Sound.load("assets/jump.wav");
        scoreSound = Sound.load("assets/score.wav");
        hitSound = Sound.load("assets/hit.wav");
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Pipe");
                placePipes();
            }
        });

        gameLoop = new Timer(1000/60, this);

    }

    public void setView(View view){
        this.view = view;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public void placePipes(){
        int randomPosY= (int) (pipeStartPosY - (pipeHeight / 4) - Math.random() * (pipeHeight / 2));
        int openingSpase = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPosY + openingSpase + pipeHeight, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void move(){
        if (!started) return;     // burung diam sampai spasi pertama
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setY(player.getY() + player.getVelocityY());
        player.setY(Math.max(player.getY(), 0));

        for (Pipe pipe : pipes) {
            pipe.setPosX(pipe.getPosX() + pipeVelocityX);

            // tabrak pipa -> game over
            if (collision(player, pipe)) {
                endGame();
            }

            // lewat pipa -> skor (hanya hitung pipa atas supaya tidak dobel)
            if (!pipe.isPassed() && pipe.getImage() == upperPipeImage
                    && pipe.getPosX() + pipe.getWidth() < player.getX()) {
                pipe.setPassed(true);
                addScore();
            }
        }

        // hapus pipa yang sudah keluar layar supaya list tidak membesar terus
        pipes.removeIf(p -> p.getPosX() + p.getWidth() < 0);

        // jatuh ke batas bawah -> game over
        if (player.getY() + player.getHeight() >= frameHeight) {
            endGame();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();

        if(view != null){
            view.repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver){
            if (!started) {
                started = true;
                pipesCooldown.restart();     // pipa baru mulai muncul sekarang
            }
            player.setVelocityY(-10);
            Sound.play(jumpSound);
        }

        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            resetGame();
        }
    }
    public void keyReleased(KeyEvent e){}

    //method cek tabrakan
    private boolean collision(Player p, Pipe pipe) {
        return p.getX() < pipe.getPosX() + pipe.getWidth()
                && p.getX() + p.getWidth() > pipe.getPosX()
                && p.getY() < pipe.getPosY() + pipe.getHeight()
                && p.getY() + p.getHeight() > pipe.getPosY();
    }

    private void endGame() {
        if (gameOver) return; // supaya tidak terpanggil dua kali

        System.out.println("GAME OVER: burungY=" + player.getY()
                + " tinggiFrame=" + frameHeight
                + " jumlahPipa=" + pipes.size());
        gameOver = true;

        gameLoop.stop();
        pipesCooldown.stop();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void resetGame() {
        player.setX(playerStartPosX);
        player.setY(playerStartPosY);
        player.setVelocityY(0);

        pipes.clear();
        pipeVelocityX = -2;      // kecepatan awal (untuk fitur kreatif)
        score = 0;
        gameOver = false;
        started = false;
        if (view != null) view.setScore(0);

        gameLoop.restart();
        pipesCooldown.stop();
    }

    private void addScore() {
        Sound.play(scoreSound);
        score++;
        if (score > highScore) highScore = score;

        // KREATIF: tiap kelipatan 5 skor, pipa makin cepat (maksimal -6)
        if (score % 5 == 0) {
            pipeVelocityX = Math.max(pipeVelocityX - 1, -6);
        }
        if (view != null) view.setScore(score);
    }

    public int getScore() { return score; }
    public int getHighScore() { return highScore; }
}
