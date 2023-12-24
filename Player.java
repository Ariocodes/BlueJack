class Player{
    public Card[] hand = new Card[4];
    public int totalValue = 0;
    public String name = "player";
    public Card lastUsedCard = new Card(0, "none", true, false); // keeping track of the last card since it's needed for speacial cards
    public Player(Card[] hnd, String userName){
        hand = hnd;
        name = userName;
    }
    public void useCard(int cardIndex){
        if(hand[cardIndex].getCardType() == "normal"){
            // adds up for normal cards
            totalValue += hand[cardIndex].value();
            lastUsedCard.setValue(hand[cardIndex].value());
            lastUsedCard.setColor(hand[cardIndex].color());
            lastUsedCard.setCardType(hand[cardIndex].getCardType());
            hand[cardIndex].setValue(0);
            hand[cardIndex].setColor("none");
            hand[cardIndex].setCardType("none");
        }
        else if(hand[cardIndex].getCardType() == "double"){
            // doubles the last used card's value
            totalValue += lastUsedCard.value();
            lastUsedCard.setColor(hand[cardIndex].color());
            lastUsedCard.setCardType(hand[cardIndex].getCardType());
            lastUsedCard.setValue(lastUsedCard.value() * 2);
            hand[cardIndex].setValue(0);
            hand[cardIndex].setColor("none");
            hand[cardIndex].setCardType("none");
        }
        else if(hand[cardIndex].getCardType() == "flip"){
            // flips the sign of the last used card
            totalValue += (lastUsedCard.value() * -2);
            lastUsedCard.setValue(lastUsedCard.value() * 2);
            lastUsedCard.setColor(hand[cardIndex].color());
            lastUsedCard.setCardType(hand[cardIndex].getCardType());
            hand[cardIndex].setValue(0);
            hand[cardIndex].setColor("none");
            hand[cardIndex].setCardType("none");
        }
    }
    public String printHand(int score){
       
        String ANSI_RESET = "";
        String ANSI_BLUE = "";
        String ANSI_YELLOW = "";
        String ANSI_RED = "";
        String ANSI_GREEN = "";
        String ANSI_PURPLE = "";
        // COLORS DON'T WORK ON CMD SO I COMMENTED THEM.
        // String ANSI_RESET = "\u001B[0m";
        // String ANSI_BLUE = "\u001B[36m";
        // String ANSI_YELLOW = "\u001B[33m";
        // String ANSI_RED = "\u001B[31m";
        // String ANSI_GREEN = "\u001B[32m";
        // String ANSI_PURPLE = "\u001B[35m";
        String text = "";
        for(int i = 0; i<hand.length; i++){
            if(hand[i].getCardType() == "normal"){
                if(hand[i].color() == "Blue"){
                    text += String.format(ANSI_BLUE + " [B%3d] " + ANSI_RESET, hand[i].value());
                }
                if(hand[i].color() == "Yellow"){
                    text += String.format(ANSI_YELLOW + " [Y%3d] " + ANSI_RESET, hand[i].value());
                }
                if(hand[i].color() == "Red"){
                    text += String.format(ANSI_RED + " [R%3d] " + ANSI_RESET, hand[i].value());
                }
                if(hand[i].color() == "Green"){
                    text += String.format(ANSI_GREEN + " [G%3d] " + ANSI_RESET, hand[i].value());
                }
            }
            else if(hand[i].getCardType() == "flip"){
                text += ANSI_PURPLE + "  [+/-] " + ANSI_RESET;
            }
            else if(hand[i].getCardType() == "double"){
                text += ANSI_PURPLE + "  [x2]  " + ANSI_RESET;
            }
            else if(hand[i].getCardType() == "none"){
                text += "   [X]  ";
            }
        }
        text += " |";
        String handN = name + "'s hand:";
        return String.format("%1$-28s| %2$s BoardTotal: %3$d    games won: %4$d", handN, text, totalValue, score);
    }
}