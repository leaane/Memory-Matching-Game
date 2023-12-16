import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;

public class Card extends JButton {
    private String front, back, folder;
    private boolean isFront;
    private int ID;

    /**
     * Constructs a card
     * @param folder the folder the card image is in
     * @param back the back of the card
     * @param front the front of the card
     * @param ID unique number identification
     */
    public Card(String folder, String back, String front, int ID) {
        super(new ImageIcon(folder + back + ".png"));
        this.folder = folder;
        this.back = back;
        this.front = front;
        this.ID = ID;
        setBorder(new MetalBorders.ButtonBorder());
        isFront = false;
    }

    /**
     * Constructs a default card
     */
    public Card() {}

    /**
     * Flips the card
     */
    public void flip() {
        if (!isFront) {
            setIcon(new ImageIcon(folder + front + ".png"));
            isFront = true;
        } else {
            setIcon(new ImageIcon(folder + back + ".png"));
            isFront = false;
        }
    }

    /**
     * Gets the front of the card
     * @return the front of the card
     */
    public String getCard() {
        return front;
    }

    /**
     * Gets the card's ID
     * @return the card's ID
     */
    public int getID() {
        return ID;
    }

}