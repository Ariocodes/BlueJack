class Card{
    private int val;
    private String col;
    private boolean positive;
    public Card(int InputValue, String InputColor, boolean InputSign){
        col = InputColor;
        positive = InputSign;
        if(!positive){
            val = InputValue * -1;
        }
        else{
            val = InputValue;
        }
    }
    public int value(){
        return val;
    }
    public String color(){
        return col;
    }
}

public class Bluejack{
    public static Card[] DeckMaker(){
        Card[] deck = new Card[40];
        int number = 0;
        for(int i = 0; i<4; i++){ //making four decks
            String color = null;
            switch(i){
                case 0:
                    color = "Blue";
                    break;
                case 1:
                    color = "Yellow";
                    break;
                case 2:
                    color = "Red";
                    break;
                case 3:
                    color = "Green";
                    break;
            }
            for(int j = 0; j<10; j++){ //making a class object for each card
                deck[number] = new Card(j+1, color, true);
                number++;
            }
        }
        return deck;
    }

    public static void main(String[] args){
        Card[] gameDeck = DeckMaker();
        
        for(int j = 0; j<gameDeck.length; j++){
            System.out.println(gameDeck[j].value() + " " + gameDeck[j].color());
        }
    }
}