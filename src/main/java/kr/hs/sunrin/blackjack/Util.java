package kr.hs.sunrin.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static int getSum(List<Card> cards) {
        int sum = 0;
        int num;
        List<Integer> aceIndexList = new ArrayList<Integer>();

        for(int i = 0; i < cards.size(); i++) {
            num = cards.get(i).getNumber();

            if(num == Card.NUMBER_ACE) aceIndexList.add(i);

            sum += num;
        }

        while (true) {
            if (sum > 21 && !aceIndexList.isEmpty()) {
                aceIndexList.remove(0);
                sum -= 10;
            }
            else break;
        }

        return sum;
    }

    public static String toStringCards(List<Card> cards, boolean hideSecondCard) {
        String str = "[  ";

        for(int i = 0; i < cards.size(); i++) {
            str += hideSecondCard && i == 1 ? "?  " : cards.get(i).toString() + "  ";
        }

        return str += "]";
    }
}
