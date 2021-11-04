package kr.hs.sunrin.blackjack;

public class Main {
    public static void main(String args[]) {
        Table table = new Table(new Decks(6), new User("player1", 100000));

        while (true) {
            //System.out.println(table.getStatus());
            if(table.getStatus() == table.STATUS_BETTING) table.bet();
            else if(table.getStatus() == table.STATUS_DEAL) table.dealCards();
            else if(table.getStatus() == table.STATUS_INSURANCE) table.insurance();
            else if(table.getStatus() == table.STATUS_ACTION) table.action();
            else if(table.getStatus() == table.STATUS_DRAW_CARDS_DEALER) table.drawCardsDealer();
        }
    }
}
