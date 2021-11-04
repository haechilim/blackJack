package kr.hs.sunrin.blackjack;

public class Card {
    public static int MAX_NUMBER = 14;
    public static int SHAPE_COUNT = 4;

    public static int SPADE = 0;
    public static int DIAMOND = 1;
    public static int HEART = 2;
    public static int CLOVER = 3;

    public static int NUMBER_ACE = 11;

    private int shape;
    private int number;

    public Card(int shape, int number) {
        this.shape = shape;
        this.number = number;
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public int getNumber() {
        if(number == 11 || number == 12 || number == 13) return 10;
        else if(number == 14) return 11;
        else return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toString() {
        return toStringShape(shape) + " " + toStringNumber(number);
    }

    private String toStringShape(int shape) {
        if(shape == SPADE) return "♠";
        else if(shape == DIAMOND) return "◇";
        else if(shape == HEART) return "♡";
        else if(shape == CLOVER) return "♣";

        return "";
    }

    private String toStringNumber(int number) {
        if(number == 11) return "J";
        else if(number == 12) return "Q";
        else if(number == 13) return "K";
        else if(number == 14) return "A";
        else return number + "";
    }
}
