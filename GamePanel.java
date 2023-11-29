import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Random;
import java.util.TimerTask;


public class GamePanel extends JPanel implements ActionListener{

//         Images and Sound     \\

    Image apple = new ImageIcon("images\\apple.png").getImage();
    Image background = new ImageIcon("images\\background.jpg").getImage();
    ImageIcon sound = new ImageIcon("images\\sound.png");
    ImageIcon soundoff = new ImageIcon("images\\soundoff.png");

    File eatSound = new File("audio\\eat.wav");
    File gameMusic = new File("audio\\music.wav");
    AudioInputStream eat = AudioSystem.getAudioInputStream(eatSound);
    AudioInputStream mus = AudioSystem.getAudioInputStream(gameMusic);
    Clip eating = AudioSystem.getClip();
    Clip music = AudioSystem.getClip();
    JButton soundButton;


//          Images and Sound         \\

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE=25;
    static final int GAME_UNITS =(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY=75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    private int bodyPart=6;
    private int applesEaten=0;
    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running =false;
    Timer timer;
    Random random;
    private int arcStart=270;
    private int arcX;
    private int arcY;
    GamePanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        soundButton = new JButton();
        soundButton.setBounds(SCREEN_WIDTH-35,SCREEN_HEIGHT+10,25,25);
        soundButton.setFocusable(false);
        soundButton.setBackground(new Color(0,0,0, 0));
        soundButton.addActionListener(this);
        soundButton.setIcon(sound);
        soundButton.setBorder(null);

        random = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT+50));
        this.setBackground(new Color(32, 128, 12));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.add(soundButton);
        this.setLayout(null);




        music.open(mus);
        eating.open(eat);
        music.start();
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
            g.drawImage(background,0,0,null);
            // Matrix as a option for background design
//            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
//                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
//            }
//            for(int i=0;i<SCREEN_WIDTH/UNIT_SIZE;i++){
//                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
//            }
            g.drawImage(apple,appleX,appleY,null);

            for(int i=0;i<bodyPart;i++){
                if(i==0){
                    g.setColor(new Color(0, 225, 169));
                    g.fillArc(x[i]+arcX,y[i]+arcY,UNIT_SIZE,UNIT_SIZE,arcStart,180);
                }
                else{
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
            }
            g.setColor(Color.WHITE);
            g.drawImage(apple,20,SCREEN_HEIGHT+10,null);
            g.drawString(":"+String.valueOf(applesEaten),40,SCREEN_HEIGHT+35);
        }
        else {
            gameOver(g);
        }
    }
    public void newApple(){
        boolean appleOnSnake = true;

        while (appleOnSnake){
            appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
            appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

            appleOnSnake=false;
            for(int i=0;i<bodyPart;i++){
                if((x[i]==appleX)&&(y[i]==appleY))
                {
                    appleOnSnake=true;
                    break;
                }
            }
        }

    }
    public void move(){
        for(int i=bodyPart;i>0;i--){
            x[i] = x[i-1];
            y[i]= y[i-1];
        }
        switch (direction){
            case 'U':y[0] = y[0] - UNIT_SIZE;
                arcStart=0;
                arcX =0;
                arcY=(UNIT_SIZE/2+1);
            break;
            case 'B':y[0] = y[0] + UNIT_SIZE;
                arcStart=180;
                arcX =0;
                arcY=(UNIT_SIZE/2+1)*-1;
            break;
            case 'L':x[0] = x[0]-UNIT_SIZE;
                arcStart=90;
                arcX =(UNIT_SIZE/2+1);
                arcY=0;
            break;
            case 'R':x[0] = x[0]+UNIT_SIZE;
                arcStart=270;
                arcX =(UNIT_SIZE/2+1)*-1;
                arcY=0;
            break;
        }
        switch (direction){
            case 'W':y[0] = y[0] - UNIT_SIZE;
                arcStart=0;
                arcX =0;
                arcY=(UNIT_SIZE/2+1);
                break;
            case 'S':y[0] = y[0] + UNIT_SIZE;
                arcStart=180;
                arcX =0;
                arcY=(UNIT_SIZE/2+1)*-1;
                break;
            case 'A':x[0] = x[0]-UNIT_SIZE;
                arcStart=90;
                arcX =(UNIT_SIZE/2+1);
                arcY=0;
                break;
            case 'D':x[0] = x[0]+UNIT_SIZE;
                arcStart=270;
                arcX =(UNIT_SIZE/2+1)*-1;
                arcY=0;
                break;
        }


    }
    public void checkApple() {
        if((x[0]==appleX)&&(y[0]==appleY)){
            bodyPart++;
            applesEaten++;
            newApple();

            eating.setMicrosecondPosition(0);
            eating.start();
        }
    }
    public void checkCollisions(){
        // Check if Head Colises with body
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
        if(x[0]>=SCREEN_WIDTH){
            running=false;
        }
        //Check if Head Touches Bottom Border
        if(y[0]>=SCREEN_HEIGHT){
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
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH-metrics1.stringWidth("Game Over"))/2,SCREEN_HEIGHT/4);
        g.setFont(new Font("Consolas",Font.BOLD,35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten,(SCREEN_WIDTH-metrics2.stringWidth("Score: "+applesEaten))/2,SCREEN_HEIGHT/3);
        g.drawString("Press Space To Restart",(SCREEN_WIDTH-metrics2.stringWidth("Press Space To Restart"))/2,SCREEN_HEIGHT/2);
    }


    public void restartGame(){
        running=false;
        bodyPart=6;
        applesEaten=0;
        direction='R';
        newApple();
        for(int i=0;i<bodyPart;i++){
            x[i]=0;
            y[i]=0;
        }
        running=true;
        timer.start();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==soundButton){
            if(music.isActive()){
                music.stop();
                soundButton.setIcon(soundoff);
            }
            else {
                music.start();
                soundButton.setIcon(sound);
            }
        }

        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }


    public class MyKeyAdapter extends KeyAdapter {

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
                    if(direction != 'B'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction='B';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(!running){
                        restartGame();
                    }
                    break;
            }
            }
            public void keyTyped(KeyEvent e){
            char c = Character.toUpperCase(e.getKeyChar());
                switch (c){
                    case 'W':
                        if(direction!='S'){
                            direction='W';
                        }
                        break;
                    case 'S':
                        if(direction!='W'){
                            direction='S';
                        }
                        break;
                    case 'A':
                        if (direction!='D'){
                            direction='A';
                        }
                        break;
                    case 'D':
                        if (direction!='A'){
                            direction='D';
                        }
                    break;
                }
            }

        }

    public int getScore(){return applesEaten;}
    }

