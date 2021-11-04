package kr.hs.sunrin.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Decks {
    private List<Card> decks = new ArrayList<Card>();
    private int deckCount;

    public Decks(int deckCount) {
        this.deckCount = deckCount;
        newDecks(deckCount);
    }

    public Card drawCard() {
        if(decks.size() == 0) newDecks(deckCount);

        int index = (int)(Math.random() * decks.size());
        Card card = decks.get(index);
        decks.remove(index);

        return card;
    }

    private void newDecks(int deckCount) {
        for(int i = 0; i < deckCount; i++) {
            addDeck();
        }
    }

    private void addDeck() {
        for(int shape = 0; shape < Card.SHAPE_COUNT; shape++) {
            for(int number = 2; number <= Card.MAX_NUMBER; number++) {
                decks.add(new Card(shape, number));
            }
        }
    }

    public List<Card> getDecks() {
        return decks;
    }

    public void setDecks(List<Card> decks) {
        this.decks = decks;
    }
}
