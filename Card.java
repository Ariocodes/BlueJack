import java.util.Random;

class Card{
    private int val;
    private String col;
    private boolean positive;
    private String cardType;
    public Card(int InputValue, String InputColor, boolean InputSign, boolean isLuckyCard){
        if(isLuckyCard){ // for lucky cards
            Random rand = new Random();
            col = InputColor; // set as none
            val = InputValue; // set as 0
            int cardChance = rand.nextInt(1, 3);
            switch(cardChance){
                case 1:
                    cardType = "flip";
                    break;
                case 2:
                    cardType = "double";
                    break;
            }
        }
        else{ // for normal cards
            cardType = "normal";
            col = InputColor;
            if(!InputSign){
                val = InputValue * -1;
            }
            else{
                val = InputValue;
            }
        }
    }
    public int value(){
        return val;
    }
    public String color(){
        return col;
    }
    public String getCardType(){
        return cardType;
    }
}
