
import javax.swing.JFrame;

//WINDOW FOR SHOWING GAME
public class App {

    public static void main(String[] args) throws Exception {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("PAC MAN");
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        //TERMINATTE THE WINDOW ONCE CLICKED ON CLOSE BUTTON
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //CREATE INSTANCE OF PACMAN CLASS
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        //FOR KEY EVENT LISTENERS
        pacmanGame.requestFocus();
        frame.setVisible(true);

    }
}
