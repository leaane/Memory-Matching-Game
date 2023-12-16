import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {
    //Screen
    private JFrame frame = new JFrame("Memory Game");
    private JPanel startScreen = new JPanel();

    //Menus
    private JPanel modeMenu = new JPanel();
    private JPanel difficultyMenu = new JPanel();
    private JPanel themeMenu = new JPanel();

    //Button layout for menus
    private JPanel gameMode = new JPanel();
    private JPanel difficulty = new JPanel();
    private JPanel themes = new JPanel();

    //Game modes
    private JButton onePlayer = new JButton("One Player");
    private JButton twoPlayer = new JButton("Two Players");

    //Difficulty levels
    private JButton easy = new JButton("Easy");
    private JButton medium = new JButton("Medium");
    private JButton hard = new JButton("Hard");

    //Themes
    private JButton cards = new JButton("Cartoons");
    private JButton biscuit = new JButton("Biscuit");
    private JButton surprise = new JButton("Surprise");

    //Labels
    private JLabel modeLabel = new JLabel("Game Mode");
    private JLabel difficultyLabel = new JLabel("Difficulty Level");
    private JLabel themeLabel = new JLabel("Theme");

    private JButton start = new JButton("Start");
    private boolean modePicked = false, difficultyPicked = false, themePicked = false;
    private String modeText, difficultyText, themeText;

    private static final Color green = new Color(67, 171, 95);

    /**
     * Constructs a default menu
     */
    public Menu() {
        //Set up screen
        frame.setSize(500,200);
        frame.setLocation(500,200);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startScreen.setLayout(new BorderLayout());
        modeMenu.setLayout(new FlowLayout(FlowLayout.CENTER));
        gameMode.setLayout(new FlowLayout(FlowLayout.CENTER));
        difficultyMenu.setLayout(new FlowLayout(FlowLayout.CENTER));

        //Sets up difficulty mode
        startScreen.add(modeMenu, BorderLayout.NORTH);
        modeMenu.add(modeLabel);
        modeMenu.add(gameMode, BorderLayout.NORTH);
        gameMode.add(onePlayer, BorderLayout.NORTH);
        gameMode.add(twoPlayer, BorderLayout.SOUTH);
        easy.addActionListener(this);
        easy.setEnabled(true);
        medium.addActionListener(this);
        medium.setEnabled(true);
        hard.addActionListener(this);
        hard.setEnabled(true);

        //Sets up game level
        startScreen.add(difficultyMenu, BorderLayout.CENTER);
        difficultyMenu.add(difficultyLabel);
        difficultyMenu.add(difficulty, BorderLayout.NORTH);
        difficulty.add(easy, BorderLayout.NORTH);
        difficulty.add(medium, BorderLayout.CENTER);
        difficulty.add(hard, BorderLayout.SOUTH);
        onePlayer.addActionListener(this);
        onePlayer.setEnabled(true);
        twoPlayer.addActionListener(this);
        twoPlayer.setEnabled(true);

        //Sets up theme
        startScreen.add(themeMenu, BorderLayout.SOUTH);
        themeMenu.add(themeLabel);
        themeMenu.add(themes, BorderLayout.NORTH);
        themes.add(cards, BorderLayout.NORTH);
        themes.add(biscuit, BorderLayout.CENTER);
        themes.add(surprise, BorderLayout.SOUTH);
        cards.addActionListener(this);
        cards.setEnabled(true);
        biscuit.addActionListener(this);
        biscuit.setEnabled(true);
        surprise.addActionListener(this);
        surprise.setEnabled(true);

        //Start button
        start.addActionListener(this);
        start.setEnabled(true);
        frame.add(start, BorderLayout.SOUTH);

        //Adds everything to screen
        frame.add(startScreen, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Ensures user picks a game mode, difficulty level, and theme before they can start
     * @return true or false
     */
    public boolean canStart() {
        return difficultyPicked && modePicked && themePicked;
    }

    /**
     * Designates tasks based on which button is clicked
     * @param click the button clicked
     */
    public void actionPerformed(ActionEvent click) {
        Object button = click.getSource();

        //Start button
        if (button == start && canStart()) {
            new MatchingGame(modeText, difficultyText, themeText);
            frame.setVisible(false);
        }

        //Difficulty buttons
        if (button == easy) {
            difficultyText = "easy";
            difficultyPicked = true;
            easy.setForeground(green);
            medium.setForeground(Color.BLACK);
            hard.setForeground(Color.BLACK);
        } else if (button == medium) {
            difficultyText = "medium";
            difficultyPicked = true;
            medium.setForeground(green);
            easy.setForeground(Color.BLACK);
            hard.setForeground(Color.BLACK);
        } else if (button == hard) {
            difficultyText = "hard";
            difficultyPicked = true;
            hard.setForeground(green);
            medium.setForeground(Color.BLACK);
            easy.setForeground(Color.BLACK);
        }

        //Mode buttons
        if (button == onePlayer) {
            modeText = "one player";
            modePicked = true;
            onePlayer.setForeground(green);
            twoPlayer.setForeground(Color.BLACK);
        } else if (button == twoPlayer) {
            modeText = "two player";
            modePicked = true;
            twoPlayer.setForeground(green);
            onePlayer.setForeground(Color.BLACK);
        }

        //Theme buttons
        if (button == cards) {
            themeText = "cards";
            themePicked = true;
            cards.setForeground(green);
            biscuit.setForeground(Color.BLACK);
            surprise.setForeground(Color.BLACK);
        } else if (button == biscuit) {
            themeText = "biscuit";
            themePicked = true;
            biscuit.setForeground(green);
            cards.setForeground(Color.BLACK);
            surprise.setForeground(Color.BLACK);
        } else if (button == surprise) {
            themeText = "surprise";
            themePicked = true;
            surprise.setForeground(green);
            biscuit.setForeground(Color.BLACK);
            cards.setForeground(Color.BLACK);
        }
    }

}