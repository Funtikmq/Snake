import javax.swing.*;

public class GameFrame extends JFrame {

    GameFrame(){
        ImageIcon icon = new ImageIcon("images\\logo.png");

        this.setIconImage(icon.getImage());
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
