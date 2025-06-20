// Ensure all imports are at the top of the file

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import javax.swing.Timer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PacMan extends JPanel implements ActionListener, KeyListener {

    //BLOCK CLASS FOR HASHSET
    class Block {

        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;

        //BASICALLY PACMAN MOVING DIRECTIONS
        //HERE DIRECTION CAN BE U D L R BASED ON THE KEYEVENT LISTENER WE UPDATE THE DIRECTIONS
        char direction = 'U';
        int velocityX = 0;
        int velocityY = 0;

        //CONSTRUCTOR
        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;

            this.startX = x;
            this.startY = y;
        }

        //to move the pacmam positions
        void updateDirection(char direction) {
            //PACMAN WILL MOVE IN LEFT RIGHT UP DOWN DIRECTION ONLY WHEN IT CAN ABLE TO MOVE IF THERE IS A WALL ON LEFT THEN PACMAN CANT MOVE IN LEFT SIDE 
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();

            //DONT ALLOW GHOST OR PACMAN TO COLLIDE WITH WALL 
            this.x += this.velocityX;
            this.y += this.velocityY;
            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }

        }

        //UPPER -Y
        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize / 4;
            } //DOWN +Y
            else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize / 4;
            } //LEFT -X
            else if (this.direction == 'L') {
                this.velocityX = -tileSize / 4;
                this.velocityY = 0;
            } //RIGHT +X
            else if (this.direction == 'R') {
                this.velocityX = tileSize / 4;
                this.velocityY = 0;
            }
        }

    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    //STORE IMAGES
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    //STORE PACMAN IMAGES
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    //TILE MAP CREATION
    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    /*
    •	O REPRESENTS SKIP
    •	EMPTY SPACE REPRESENTS FOOD
    •	X REPRESENTS WALL
    •	P represents PAC MAN
    •	AND r b p  REPRESENTS GHOST
     */
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    //HASHSET<CLASS DATATYPE> name
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;

    // Constructor
    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        ////FOR KEY EVENT LISTENERS
        //this refers to pacman object and it listens key presses and process thrree functions such as release,pressed that are defined below
        addKeyListener(this);
        setFocusable(true);

        //load all images from src folder
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap();

        //SETTIN G  A GAME LOOP  TIMER THAT REPAINT UR WHOLE UI AT EACH 50 MILLI SECOND
        gameLoop = new Timer(50, this);
        gameLoop.start();

        //TOTAL WALLS FOODS AND GHOSTS ON TILE MAP
        System.out.println(walls.size());
        System.out.println(foods.size());
        System.out.println(ghosts.size());

    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        //TRAVERSING THE TILE MAP
        /*
         * r represents row and c represents columns
         */
        for (int r = 0; r < rowCount; r++) {

            for (int c = 0; c < columnCount; c++) {
                //FIRST ROW MEANS 0TH POSTION STRING ROW
                String row = tileMap[r];
                //CURRENT COLUMN CHARACTER
                char tileMapChar = row.charAt(c);

                //X AND Y REPRESENTS OR FOR IDENTIFYING POSTIONS
                //x represents columns horizontal
                //y represents rows vertical
                int x = c * tileSize;
                int y = r * tileSize;

                //NOW START ADDING WALLS,FOOD,EMPTY SPACE,RED,BLUE GHOST
                if (tileMapChar == 'X') {
                    //FIRST OF ALL ADD ALL DETAILS TO BLOCK CONSTRUCTOR
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    //THEN SET WALL OBJECT IN WALLS HASHSET
                    walls.add(wall);
                } else if (tileMapChar == 'b') {
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'o') {
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'r') {
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'p') {
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                } else if (tileMapChar == 'P') {
                    //NO NEED TO ADD IT IN HASH SET AS THERE IS ONLY ONE PACMAN
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                } else if (tileMapChar == ' ') {
                    Block food = new Block(null, x + 14, y + 14, 4, 4);
                    foods.add(food);
                }

            }
        }

    }

    //PRINT THE IMAGES ON JPANEL
    public void paintComponent(Graphics g) {
        //WE ARE GETTING THIS FUNCTION paintComponent FROM JPanel
        super.paintComponent(g);
        draw(g);
    }

    //BASICALLY IT DRAWS IMAGES OF WALLS GHOSTS PACMAN ON JPANEL AND IT CAN BE DONE USING GRAPHICS OBJECT
    public void draw(Graphics g) {

        //PACMAN
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        //GHOST HASHSET OBJECT CONTAIN N OBJECT OF ghost 
        //WALLS HASHSET OBJECT CONTAINS N OBJECT OF walls
        //FOODS HASHSET OBJECT CONTAINS N OBJECT OF foods
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
    }

    //NOW UPDATE THE POSTIONS OF PACMAN OR MOVE IT
    //THIS FUNCTION IS CALLED WHENEVER 
    public void move() {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        //CHECK WALL COLISIONS
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

    }

    //COLLISIONS OF GHOST AND WALL WITH PACMAN
    //COLLISION FORMULA
    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width
                && a.x + a.width > b.x
                && a.y < b.y + b.height
                && a.y + a.height > b.y;
    }

    //LOGIC OF GAME 
    //REPAINTING THE UI
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    //when u click on UP,DOWN,LEFT,RIGHTkey it will update velocity which will help to update the postions(x,y) of pacman 
    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("KEY EVENT: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }
    }

}
