import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

public class MatchingGame extends JFrame implements ActionListener {
    private JFrame frame = new JFrame("Memory Game");
    private JPanel game = new JPanel();
    private JPanel score = new JPanel();
    private JLabel scoreLabel = new JLabel();
    private JLabel scoreLabel2 = new JLabel();
    private JLabel scoreAILabel = new JLabel();
    private JButton back = new JButton("Back");
    private Card[] cards;
    private int totalCards;
    private String theme;


    //Gameplay variables
    private Random random = new Random();
    private ArrayList<Card> AIMemory = new ArrayList<>();
    private Card card1 = new Card(), card2 = new Card(), AICard1 = new Card(), AICard2 = new Card();
    private boolean isTwoPlayer = false, easy = false, medium = false, hard = false;
    private boolean oneCardFlipped = false, cardsFlipping = false, AITurn = false, AIHasMemory = false;
    private int flips = 0, userMatches = 0, AIMatches = 0;

    /**
     * Constructs a memory matching game
     * @param mode the game mode
     * @param difficulty the difficulty level
     * @param theme the theme
     */
    public MatchingGame(String mode, String difficulty, String theme) {
        frame.setSize(800,800);
        frame.setLocation(400,300);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Initializes cards
        this.theme = theme;
        setTotalCards(difficulty);
        initializeCards();

        //Sets up game based on difficulty
        setDifficulty(difficulty);

        //Sets up game mode
        if (mode.equals("two player")) {
            isTwoPlayer = true;
        }
        createScore();

        //Sets up screen
        frame.add(game, BorderLayout.CENTER);
        frame.add(score, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /**
     * Checks whether the game is over
     * @return true or false
     */
    public boolean isGameOver() {
        for (int i = 0; i < totalCards; i++) {
            if (cards[i].isEnabled()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ends game if game is over
     */
    public void endGame() {
        if (isGameOver()) {
            if (isTwoPlayer) {
                if (AIMatches > userMatches) {
                    scoreAILabel.setText(userMatches + " vs " + AIMatches);
                    scoreLabel2.setText("The AI won :(");
                } else if (userMatches > AIMatches) {
                    scoreAILabel.setText(userMatches + " vs " + AIMatches);
                    scoreLabel2.setText("You won :)");
                } else {
                    scoreAILabel.setText("");
                    scoreLabel2.setText("Tie!");
                }
            } else {
                scoreLabel.setText("You won :)");
            }
        }
    }

    /**
     * Operates AI based on the difficulty
     * @param difficulty the difficulty
     */
    public void AI(String difficulty) {
        Timer aiTurnTimer = new Timer(1000, e -> {
            //Ends game if user found last match
            if (isGameOver()) {
                endGame();
                return;
            }
        switch (difficulty) {
            case "easy" -> { //AI has no memory (Randomly flips every turn)
                int n = random.nextInt(totalCards);
                while ((!cards[n].isEnabled())) { //Card has to be enabled
                    n = random.nextInt(totalCards);
                }
                int m = random.nextInt(totalCards);
                while ((m == n) || (!cards[m].isEnabled()) || ((cards[n] == card1) && (cards[m] == card2))
                || ((cards[m] == card1) && (cards[n] == card2))) { //Card has to be enabled and can't be the previous or user's
                    m = random.nextInt(totalCards);
                }

                AICard1 = cards[n];
                AICard2 = cards[m];

                //Flips after user's cards are flipped back
                Timer cardFlipTimer = new Timer(1000, a -> {
                    AICard1.flip();
                    AICard2.flip();
                    ((Timer) a.getSource()).stop();
                });
                cardFlipTimer.setRepeats(false);
                cardFlipTimer.start();
            }
            case "medium" -> { //AI has some memory (50%  chance of getting a match if card is in memory)
                //When AI has no memory
                if (!AIHasMemory) { //Starts off like easy mode
                    int n = random.nextInt(totalCards);
                    while ((!cards[n].isEnabled())) {
                        n = random.nextInt(totalCards);
                    }
                    int m = random.nextInt(totalCards);
                    while ((m == n) || (!cards[m].isEnabled()) || ((cards[n] == card1) && (cards[m] == card2))
                            || ((cards[m] == card1) && (cards[n] == card2))) {
                        m = random.nextInt(totalCards);
                    }

                    AICard1 = cards[n];
                    AICard2 = cards[m];

                    Timer cardFlipTimer = new Timer(1000, a -> {
                        AICard1.flip();
                        AICard2.flip();
                        ((Timer) a.getSource()).stop();
                    });
                    cardFlipTimer.setRepeats(false);
                    cardFlipTimer.start();

                    AIMemory.add(AICard1);
                    AIMemory.add(AICard2);
                }

                //When AI has memory
                if (AIHasMemory) {
                    boolean hasMatch = false;
                    int n = random.nextInt(totalCards);
                    while (!cards[n].isEnabled()) {
                        n = random.nextInt(totalCards);
                    }

                    //If n card is in memory, match it at a 50% chance
                    for (int i = 0; i < AIMemory.size(); i++) {
                        if ((cards[n].getCard().equals(AIMemory.get(i).getCard())) &&
                           (cards[n].getID() != AIMemory.get(i).getID()) && (AIMemory.get(i).isEnabled())) {
                            int r = random.nextInt(2);
                            if (r == 1) {
                                AICard1 = cards[n];
                                AICard2 = AIMemory.get(i);
                                Timer cardFlipTimer = new Timer(1000, a -> {
                                    AICard1.flip();
                                    AICard2.flip();
                                    ((Timer) a.getSource()).stop();
                                });
                                cardFlipTimer.setRepeats(false);
                                cardFlipTimer.start();
                                AIMemory.remove(i);
                                hasMatch = true;
                            }
                            break;
                        }
                    }

                    //If no match, randomly pick a second card and add both cards to memory
                    if (!hasMatch) {
                        int m = random.nextInt(totalCards);
                        while ((m == n) || (!cards[m].isEnabled()) || ((cards[n] == card1) && (cards[m] == card2)) ||
                               ((cards[m] == card1) && (cards[n] == card2))) {
                            m = random.nextInt(totalCards);
                        }

                        AICard1 = cards[n];
                        AICard2 = cards[m];
                        Timer cardFlipTimer = new Timer(1000, a -> {
                            AICard1.flip();
                            AICard2.flip();
                            ((Timer) a.getSource()).stop();
                        });
                        cardFlipTimer.setRepeats(false);
                        cardFlipTimer.start();

                        AIMemory.add(AICard1);
                        AIMemory.add(AICard2);
                    }
                }
                AIHasMemory = true;
            }
            case "hard" -> { //AI has memory (100% chance of getting a match if card is in memory)
                //When AI has no memory
                if (!AIHasMemory) { //Starts off like easy mode
                    int n = random.nextInt(totalCards);
                    while ((!cards[n].isEnabled())) {
                        n = random.nextInt(totalCards);
                    }
                    int m = random.nextInt(totalCards);
                    while ((m == n) || (!cards[m].isEnabled()) || ((cards[n] == card1) && (cards[m] == card2)) ||
                          ((cards[m] == card1) && (cards[n] == card2))) {
                        m = random.nextInt(totalCards);
                    }

                    AICard1 = cards[n];
                    AICard2 = cards[m];

                    //Flips after user's cards are flipped back
                    Timer cardFlipTimer = new Timer(1000, a -> {
                        AICard1.flip();
                        AICard2.flip();
                        ((Timer) a.getSource()).stop();
                    });
                    cardFlipTimer.setRepeats(false);
                    cardFlipTimer.start();

                    AIMemory.add(AICard1);
                    AIMemory.add(AICard2);
                }

                //When AI has memory
                if (AIHasMemory) {
                    boolean hasMatch = false;
                    int n = random.nextInt(totalCards);
                    while (!cards[n].isEnabled()) {
                        n = random.nextInt(totalCards);
                    }

                    //If n card is in memory, match it
                    if (!AIMemory.isEmpty()) {
                        for (int i = 0; i < AIMemory.size(); i++) {
                            if ((cards[n].getCard().equals(AIMemory.get(i).getCard())) &&
                               (cards[n].getID() != AIMemory.get(i).getID()) && (AIMemory.get(i).isEnabled())) {
                                AICard1 = cards[n];
                                AICard2 = AIMemory.get(i);
                                Timer cardFlipTimer = new Timer(1000, a -> {
                                    AICard1.flip();
                                    AICard2.flip();
                                    ((Timer) a.getSource()).stop();
                                });
                                cardFlipTimer.setRepeats(false);
                                cardFlipTimer.start();
                                AIMemory.remove(i);
                                hasMatch = true;
                                break;
                            }
                        }
                    }

                    //If no match, randomly pick a second card and add both cards to memory
                    if (!hasMatch) {
                        int m = random.nextInt(totalCards);
                        while ((m == n) || (!cards[m].isEnabled()) || ((cards[n] == card1) && (cards[m] == card2)) ||
                               ((cards[m] == card1) && (cards[n] == card2))) {
                            m = random.nextInt(totalCards);
                        }

                        AICard1 = cards[n];
                        AICard2 = cards[m];
                        Timer cardFlipTimer = new Timer(1000, a -> {
                            AICard1.flip();
                            AICard2.flip();
                            ((Timer) a.getSource()).stop();
                        });
                        cardFlipTimer.setRepeats(false);
                        cardFlipTimer.start();

                        AIMemory.add(AICard1);
                        AIMemory.add(AICard2);
                    }
                }
                AIHasMemory = true;
            }
        }
            cardsFlipping = true; //Prevents user from flipping until AI's turn is over

            //Checks for a match
            Timer cardFlipTimer;
            if (AICard1.getCard().equals(AICard2.getCard())) {
                cardFlipTimer = new Timer(2000, event -> {
                    scoreAILabel.setText("AI Matches: " + ++AIMatches);
                    AICard1.setEnabled(false);
                    AICard2.setEnabled(false);
                    ((Timer) event.getSource()).stop();
                    cardsFlipping = false;
                    endGame();
                });
            } else {
                cardFlipTimer = new Timer(2000, event -> {
                    AICard1.flip();
                    AICard2.flip();
                    ((Timer) event.getSource()).stop();
                    cardsFlipping = false;
                });
            }
            cardFlipTimer.setRepeats(false);
            cardFlipTimer.start();
        });
        aiTurnTimer.setRepeats(false);
        aiTurnTimer.start();
    }

    /**
     * Creates score tracking
     */
    public void createScore() {
        score.setLayout(new GridLayout(2, 1));
        if (!isTwoPlayer) {
            scoreLabel.setText("Flips : " + flips);
            scoreLabel.setBorder(new EmptyBorder(0,10,0,10));
            scoreLabel.setFont(new Font("Calibri", Font.BOLD, 40));
            score.add(scoreLabel,BorderLayout.CENTER);
        } else {
            scoreLabel2.setText("Your Matches : " + userMatches);
            scoreLabel2.setBorder(new EmptyBorder(0,10,0,10));
            scoreLabel2.setFont(new Font("Calibri", Font.BOLD, 40));
            score.add(scoreLabel2,BorderLayout.CENTER);
            scoreAILabel = new JLabel("AI Matches: " + AIMatches);
            scoreAILabel.setBorder(new EmptyBorder(0,10,0,10));
            scoreAILabel.setFont(new Font("Calibri", Font.BOLD, 40));
            score.add(scoreAILabel,BorderLayout.SOUTH);
        }
        back.addActionListener(this);
        back.setEnabled(true);
        score.add(back, BorderLayout.EAST);
    }

    /**
     * Creates memory game board
     * @param rows the amount of rows
     * @param columns the amount of columns
     */
    public void createGame(int rows, int columns) {
        game.setLayout(new GridLayout(rows, columns, 10, 10));
        game.setMaximumSize(new Dimension(800,800));
        game.setBorder(new EmptyBorder(10,10,10,10));

        for (int i = 0; i < totalCards; i++) {
            cards[i].addActionListener(this);
            cards[i].setEnabled(true);
            game.add(this.cards[i]);
        }
    }

    /**
     * Initializes cards
     */
    public void initializeCards() {
        cards = new Card[totalCards];
        for (int i = 0, j = 1; i < totalCards/2; i++, j++) {
            cards[2*i] = new Card("Images/" + theme + "/", theme,theme+j, i);
            cards[2*i + 1] = new Card("Images/" + theme + "/", theme,theme+j, i + totalCards/2);
        }
        shuffleCards();
    }

    /**
     * Shuffles cards
     */
    public void shuffleCards() {
        List<Card> temp = Arrays.asList(cards);
        Collections.shuffle(temp);
        cards = temp.toArray(new Card[0]);
    }

    /**
     * Sets total cards based on difficulty level
     * @param difficulty the difficulty level
     */
    public void setTotalCards(String difficulty) {
        switch (difficulty) {
            case "easy" -> totalCards = 10;
            case "medium" -> totalCards = 16;
            case "hard" -> totalCards = 24;
        }
    }

    /**
     * Sets the difficulty level of the game
     * @param difficulty the difficulty level
     */
    public void setDifficulty(String difficulty) {
        switch (difficulty) {
            case "easy" -> {createGame(2, 5); easy = true;}
            case "medium" -> {createGame(4, 4); medium = true;}
            case "hard" -> {createGame(4, 6); hard = true;}
        }
    }

    /**
     * Designates tasks based on which button is clicked
     * @param click the button clicked
     */
    public void actionPerformed(ActionEvent click) {
        Object button = click.getSource();

        //Prevents user from flipping until it's their turn
        if (cardsFlipping) {
            return;
        }

        //Back to menu
        if (button == back) {
            new Menu();
            frame.setVisible(false);
        }

        //User gameplay
        for (int i = 0; i < totalCards; i++) {
            //Gets first card
            if ((button == cards[i]) && (!oneCardFlipped)) {
                card1 = cards[i];
                card1.flip();
                oneCardFlipped = true;
                scoreLabel.setText("Flips: " + ++flips);
                break;
            }

            //Gets second card
            if (button == cards[i] && oneCardFlipped) {
                //Ensures the user doesn't click the same card
                if (cards[i].getID() == card1.getID()) {
                    break;
                }
                card2 = cards[i];
                card2.flip();
                scoreLabel.setText("Flips: " + ++flips);
                oneCardFlipped = false;
                AITurn = true;
                cardsFlipping = true; //Prevents user from flipping until cards are either disabled or flipped back

                //Checks for a match
                Timer cardFlipTimer;
                if (card1.getCard().equals(card2.getCard())) {
                    //Starts the timer to delay disabling the cards
                    cardFlipTimer = new Timer(1000, e -> {
                        scoreLabel2.setText("Your Matches: " + ++userMatches);
                        card1.setEnabled(false); //Disable from being flipped
                        card2.setEnabled(false);
                        ((Timer) e.getSource()).stop(); //Stops the timer after flipping back
                        cardsFlipping = false;
                        endGame();
                    });
                } else {
                    //Starts the timer to delay flipping back the cards
                    cardFlipTimer = new Timer(1000, e -> {
                        card1.flip();
                        card2.flip();
                        ((Timer) e.getSource()).stop(); //Stops the timer after flipping back
                        cardsFlipping = false;
                    });
                }
                cardFlipTimer.setRepeats(false);
                cardFlipTimer.start();
                break;
            }
        }

        //AI's turn
        if ((isTwoPlayer) && (AITurn)) {
            AITurn = false;

            //Flips two cards
            if (easy) {
                AI("easy");
            } else if (medium) {
                AI("medium");
            } else if (hard) {
                AI("hard");
            }
        }
    }

}