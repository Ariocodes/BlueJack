import java.util.Random;
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

    public static Card[] shuffle(Card[] mainArray){
        /*
        The function takes a main array of cards, uses random to select a random index and checks 
        if the index has been used before or not (previously used indexes are saved in an additional array
        so we can keep track of them.) then, if the index was not used before, it sets the value
        inside that random index of the mainarray as value inside the result array (starting from first index)
        */
        int[] alreadySaved = new int[mainArray.length]; // already saved indexes. (values are preset as 0)
        Card[] resultArray = new Card[mainArray.length];
        Random rand = new Random();
        boolean save = true;
        int randomIndex = 0;
        int savingIndex = 0; // keeping track of how many indexes have been saved (for both value setting and while loop)
        while(savingIndex < mainArray.length){
            save = true;
            randomIndex = rand.nextInt(1, mainArray.length+1); 
            /* 
            sets a random index value (from 1 to length+1) [because the value of 0 is already preset in the array 
            which the algorithm skips since it's treated as an already existing value (while the value of 0th index
            is not saved in the result array). "if(save)" fails, savingIndex never reaches the needed value to end
            the loop, and the 0th index will never be saved in the result array.]
            */
            for(int i = 0; i<alreadySaved.length; i++){
                if(alreadySaved[i] == randomIndex){ // Checking if the random index value was saved before (if already saved = don't save)
                    save = false;
                    break;
                }
            }
            if(save){
                alreadySaved[savingIndex] = randomIndex;
                resultArray[savingIndex] = mainArray[randomIndex-1]; // -1 because we chose the random index from 1 to length+1 (index0 as 1, index1 as 2 and so on.)
                savingIndex++;
            }
        }
        return resultArray;
    }

    public static void main(String[] args){
        Card[] gameDeck = DeckMaker();
        
        for(int i = 0; i<gameDeck.length; i++){
            System.out.println(gameDeck[i].value() + " " + gameDeck[i].color());
        }
    
        Card[] shuffledDeck = shuffle(gameDeck);
        System.out.println();

        for(int i = 0; i<shuffledDeck.length; i++){
            System.out.println(shuffledDeck[i].value() + " " + shuffledDeck[i].color());
        }
    }
}