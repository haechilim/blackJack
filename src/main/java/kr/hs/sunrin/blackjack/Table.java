package kr.hs.sunrin.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Table {
    //TODO 리팩토링

    public static final int MIN_BETTING = 1000;

    public static final int STATUS_BETTING = 0;
    public static final int STATUS_DEAL = 1;
    public static final int STATUS_INSURANCE = 2;
    public static final int STATUS_ACTION = 3;
    public static final int STATUS_DRAW_CARDS_DEALER = 4;

    private Decks decks;
    private int status;
    private User user;
    private List<Card> targetCards;
    private List<Card> dealerCards = new ArrayList<Card>();

    public Table(Decks decks, User user) {
        this.decks = decks;
        this.user = user;

        resetTable();
    }

    public void bet() {
        int point = 0;

        System.out.println("포인트를 배팅해 주세요");
        System.out.println("보유 포인트: " + user.getPoint() + "P");
        System.out.print("배팅할 포인트: ");
        try {
            point = new Scanner(System.in).nextInt();
        } catch (Exception e) {
            System.out.println("숫자만 입력할 수 있습니다.\n");
        }

        if(point < MIN_BETTING) System.out.println("\n" + MIN_BETTING + "P 이상만 배팅할 수 있습니다.\n");
        else if(point > user.getPoint()) System.out.println("\n보유 중인 포인트가 부족합니다.\n");
        else {
            user.bettingPoint(point);
            status = STATUS_DEAL;
        }
    }

    public void dealCards() {
        targetCards = user.getCards();

        for(int i = 0; i < 2; i++) {
            if(targetCards.size() < 2) targetCards.add(decks.drawCard());
            if(dealerCards.size() < 2) dealerCards.add(decks.drawCard());
        }

        printTable(true);

        if(checkBlackjack(targetCards)) {
            user.addPoint((int)(user.getBetPoint() * 2.5));
            System.out.println("\n블랙잭!!! " + (int)(user.getBetPoint() * 1.5) + "P를 얻었습니다.\n");
            resetTable();
            return;
        }

        status = STATUS_INSURANCE;
    }

    public void insurance() {
        if(dealerCards.size() <= 0) return;

        if(dealerCards.get(0).getNumber() == 11) {
            int answer;

            System.out.print("인셔런스(insurance) 하시겠습니까? (1) 네 (2) 아니오: ");
            try {
                answer = new Scanner(System.in).nextInt();
            } catch (Exception e) {
                System.out.println("숫자만 입력할 수 있습니다.\n");
                return;
            }

            if(answer == 1) {
                int insurance = user.getBetPoint() / 2;

                if(insurance > user.getPoint()) {
                    System.out.println("보유 포인트가 부족하여 인셔런스 할 수 없습니다.\n");
                    status = STATUS_ACTION;
                    return;
                }

                System.out.println("\n" + insurance + "P를 인셔런스 하였습니다.");
                user.bettingPoint(insurance);

                if(checkBlackjack(dealerCards)) {
                    printTable(false);
                    System.out.println("딜러가 블랙잭이므로 " + insurance * 3 + "P를 돌려받았습니다.");
                    user.addPoint(insurance * 3);
                    resetTable();
                }
                else {
                    System.out.println("딜러가 블랙잭이 아니므로 " + insurance + "P를 잃었습니다.");
                    System.out.println("현재 보유중인 포인트: " + user.getPoint() + "P\n");
                    user.setBetPoint(user.getBetPoint() - insurance);
                    status = STATUS_ACTION;
                }
            }
            else if(answer == 2) {
                if(checkBlackjack(dealerCards)) {
                    System.out.println(toString(false));
                    System.out.println("딜러가 블랙잭이므로 " + user.getBetPoint() + "P를 잃었습니다.");
                    resetTable();
                }
                else {
                    System.out.println("딜러는 블랙잭이 아닙니다.");
                    status = STATUS_ACTION;
                }
            }
            else System.out.println("1 또는 2만 입력 가능합니다.\n");
        }
        else if(dealerCards.get(0).getNumber() == 10) {
            if(checkBlackjack(dealerCards)) {
                System.out.println(toString(false));
                System.out.println("딜러가 블랙잭이므로 " + user.getBetPoint() + "P를 잃었습니다.");
                resetTable();
            }
            else {
                System.out.println("딜러는 블랙잭이 아닙니다.");
                status = STATUS_ACTION;
            }
        }
        else status = STATUS_ACTION;
    }

    public void action() {
        int action;

        System.out.println("액션 : (1) 힛(Hit) (2) 스테이(Stay) (3) 더블 다운(Double down) (4) 스플릿(Split)");
        try {
            action = new Scanner(System.in).nextInt();
        } catch (Exception e) {
            System.out.println("숫자만 입력할 수 있습니다.\n");

            return;
        }

        switch (action) {
            case 1:
                hit();
                break;

            case 2:
                stay();
                break;

            case 3:
                doubleDown();
                break;

            case 4:
                split();
                break;

            default:
                System.out.println("1 ~ 4 까지의 숫자만 입력할 수 있습니다.\n");
                break;
        }
    }

    public void drawCardsDealer() {
        if(user.getCards().isEmpty() && user.getSplit().isEmpty()) {
            resetTable();
            return;
        }

        printTable(false);

        if(Util.getSum(dealerCards) < 17) dealerCards.add(decks.drawCard());
        else if(Util.getSum(dealerCards) > 21) {
            System.out.println("딜러가 버스트(Bust) 되어 " + user.getBetPoint() + "P를 얻었습니다.");

            if(!user.getSplit().isEmpty()) {
                System.out.println("딜러가 버스트(Bust) 되어 " + user.getBetPointSplit() + "P를 얻었습니다.");
                user.addPoint(user.getBetPointSplit() * 2);
            }

            user.addPoint(user.getBetPoint() * 2);
            resetTable();
        }
        else {
            if(Util.getSum(dealerCards) < Util.getSum(targetCards)) {
                if(targetCards == user.getSplit()) {
                    System.out.println("\n딜러의 카드 총합이 플래이어의 카드 총합보다 낮아 " + user.getBetPointSplit() + "P를 얻었습니다");
                    user.addPoint(user.getBetPointSplit() * 2);
                }
                else {
                    System.out.println("\n딜러의 카드 총합이 플래이어의 카드 총합보다 낮아 " + user.getBetPoint() + "P를 얻었습니다");
                    user.addPoint(user.getBetPoint() * 2);
                }
            }
            else System.out.println("\n딜러의 카드 총합이 플래이어의 카드 총합과 같거나 높아 " + user.getBetPoint() + "P를 잃었습니다");

            if(targetCards == user.getSplit() || user.getSplit().isEmpty()) resetTable();
            else targetCards = user.getSplit();
        }
    }

    private boolean hit() {
        int point = targetCards == user.getSplit() ? user.getBetPointSplit() : user.getBetPoint();
        targetCards.add(decks.drawCard());

        printTable(true);

        if(checkBust(targetCards)) {
            System.out.println("버스트(Bust) 되어 " + point + "P를 잃었습니다.\n");

            if(targetCards == user.getSplit()) user.setBetPointSplit(0);
            else user.setBetPoint(0);

            if(targetCards == user.getSplit() || user.getSplit().isEmpty()) {
                if(user.getCards().isEmpty()) resetTable();
                targetCards.clear();
                status = STATUS_DRAW_CARDS_DEALER;
            }

            targetCards.clear();
            targetCards = user.getSplit();
            return false;
        }

        return true;
    }

    private void stay() {
        if(targetCards == user.getSplit() || user.getSplit().isEmpty()) {
            status = STATUS_DRAW_CARDS_DEALER;
            targetCards = user.getCards();
        }
        else targetCards = user.getSplit();
    }

    private void doubleDown() {
        if(user.getPoint() < user.getBetPoint()) {
            System.out.println("보유 포인트가 적어 더블 다운(Double down) 할 수 없습니다.");
            return;
        }

        user.bettingPoint(user.getBetPoint());
        if(hit()) stay();
    }

    private void split() {
        if(user.getPoint() < user.getBetPoint()) System.out.println("보유 포인트가 적어 스플릿(Split) 할 수 없습니다.");
        else if(user.getCards().size() != 2 || !user.getSplit().isEmpty()) System.out.println("\n스플릿(Split)은 최초에 한번만 할 수 있습니다.\n");
        else if(user.getCards().get(0).getNumber() != user.getCards().get(1).getNumber()) System.out.println("\n스플릿(Split)은 두 카드의 숫자가 같을때만 할 수 있습니다.\n");
        else {
            user.split();
            user.bettingPointSplit(user.getBetPoint());

            printTable(true);
        }
    }

    private boolean checkBust(List<Card> cards) {
        return Util.getSum(cards) > 21;
    }

    private boolean checkBlackjack(List<Card> cards) {
        if(cards.size() != 2) return false;

        return Util.getSum(cards) == 21;
    }

    private void resetTable() {
        System.out.println("\n-----------------------------------------------------\n");
        status = STATUS_BETTING;
        user.getCards().clear();
        user.getSplit().clear();
        user.setBetPoint(0);
        user.setBetPointSplit(0);
        dealerCards.clear();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void printTable(boolean hideSecondCard) {
        System.out.println(toString(hideSecondCard));
    }

    public String toString(boolean hideSecondCard) {
        return "\n================== table ==================\n" +
               "\n" +
               "dealer " + Util.toStringCards(dealerCards, hideSecondCard) + (hideSecondCard ? "" : ("   총합: " + Util.getSum(dealerCards))) + "\n" +
               "\n" +
               "\n" +
               "you    " + toStringCards(user.getCards(), user.getBetPoint()) + "      " + (user.getSplit().isEmpty() ? "" : toStringCards(user.getSplit(), user.getBetPointSplit())) + "\n" +
               "\n" +
               "내 포인트: " + user.getPoint() + "P\n" +
               "===========================================\n";
    }

    private String toStringCards(List<Card> cards, int point) {
        return Util.toStringCards(cards, false) + "   총합: " + Util.getSum(cards) + "   배팅된 포인트: " + point + "P";
    }
}
