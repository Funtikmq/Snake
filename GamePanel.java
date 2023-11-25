import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{


    ImageIcon apple = new ImageIcon("apple.png");

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE=60;
    static final int GAME_UNITS =(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY=150;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyPart=6;
    int applesEaten=0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running =false;
    Timer timer;
    Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.PINK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();
    }

    public void startGame(){
        newApple();
        running=true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        if(running){
            g.setColor(Color.white);
            g.setFont(new Font("Consolas",Font.BOLD,35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten,(SCREEN_WIDTH-metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
            }
            for(int i=0;i<SCREEN_WIDTH/UNIT_SIZE;i++){
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
            }
            apple.paintIcon(null,g,appleX,appleY);

            for(int i=0;i<bodyPart;i++){
                if(i==0){
                    g.setColor(new Color(23, 241, 9));
                    g.fillRect(x[i],y[i],UNIT_SIZE-1,UNIT_SIZE-1);
                }
                else {
                    g.setColor(new Color(92, 204, 85));
                    g.fillRect(x[i],y[i],UNIT_SIZE-1,UNIT_SIZE-1);
                }
            }
        }
        else {
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i=bodyPart;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'U':y[0] = y[0] - UNIT_SIZE;
            break;
            case 'D':y[0] = y[0] + UNIT_SIZE;
            break;
            case 'L':x[0] = x[0]-UNIT_SIZE;
            break;
            case 'R':x[0] = x[0]+UNIT_SIZE;
        }
    }
    public void checkApple(){
        if((x[0]==appleX)&&(y[0]==appleY)){
            bodyPart++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        // Check if Head Colides with body
        for(int i=bodyPart;i>0;i--){
            if((x[0]==x[i])&&(y[0]==y[i])){
                running=false;
            }
        }
        //Check If Head Touches Left Border
        if(x[0]<0){
            running=false;
        }
        //Check if Head Touches Right Border
        if(x[0]>SCREEN_WIDTH){
            running=false;
        }
        //Check if Head Touches Bottom Border
        if(y[0]>SCREEN_HEIGHT){
            running=false;
        }//Check if Head Touches Top Border
        if(y[0]<0){
            running=false;
        }

        if (!running){
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        //Game Over Text
        g.setColor(Color.RED);
        g.setFont(new Font("Consolas",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH-metrics1.stringWidth("Game Over"))/2,SCREEN_HEIGHT/4);
        g.setColor(Color.white);
        g.setFont(new Font("Consolas",Font.BOLD,35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten,(SCREEN_WIDTH-metrics2.stringWidth("Score: "+applesEaten))/2,SCREEN_HEIGHT/3);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }



    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction !='R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction='D';
                    }
                    break;
            }
        }
    }
}
