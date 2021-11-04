package kr.hs.sunrin.blackjack;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private int point;
    private int betPoint;
    private int betPointSplit;
    private List<Card> cards = new ArrayList<Card>();
    private List<Card> split = new ArrayList<Card>();

    public User(String name, int point) {
        this.name = name;
        this.point = point;
    }

    public void bettingPoint(int point) {
        if(point == 0 || this.point < point) return;

        this.point -= point;
        betPoint += point;
    }

    public void bettingPointSplit(int point) {
        if(point == 0 || this.point < point) return;

        this.point -= point;
        betPointSplit += point;
    }

    public void split() {
        if(cards.size() != 2) return;
        else {
            split.add(cards.get(1));
            cards.remove(1);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public int getBetPoint() {
        return betPoint;
    }

    public void setBetPoint(int betPoint) {
        this.betPoint = betPoint;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getSplit() {
        return split;
    }

    public void setSplit(List<Card> split) {
        this.split = split;
    }

    public int getBetPointSplit() {
        return betPointSplit;
    }

    public void setBetPointSplit(int betPointSplit) {
        this.betPointSplit = betPointSplit;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", point=" + point +
                ", betPoint=" + betPoint +
                ", cards=" + cards +
                ", split=" + split +
                '}';
    }
}
