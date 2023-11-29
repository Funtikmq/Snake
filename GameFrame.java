import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameFrame extends JFrame {


    GameFrame() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        ImageIcon icon = new ImageIcon("images\\logo.png");

        this.setIconImage(icon.getImage());
        this.setTitle("Snake");
        this.setBackground(new Color(157, 114, 31));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new GamePanel());
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
