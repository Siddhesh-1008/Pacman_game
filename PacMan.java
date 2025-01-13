// Ensure all imports are at the top of the file

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashSet;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PacMan extends JPanel {

    //BLOCK CLASS FOR HASHSET
    class Block {

        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;

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
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    //STORE IMAGESS
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

    // Constructor
    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

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


        for (Block food : foods) {
          g.drawImage(food.image, food.x, food.y, food.width, food.height, null);
        }
    }

}
