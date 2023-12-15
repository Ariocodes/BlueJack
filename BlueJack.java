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

public class Bluejack{
    public static Card[] DeckMaker(int amountOfCards){
        Card[] deck = new Card[amountOfCards];
        int cardNumber = 0;
        String color = null;
        for(int i = 0; i<4; i++){ //making four decks
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
                deck[cardNumber] = new Card(j+1, color, true, false);
                cardNumber++;
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
        int[] alreadySaved = new int[mainArray.length]; // indexes are already preset. (values are preset as 0)
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
            is not saved in the result array). "if(save)" on line 34 fails, savingIndex never reaches the needed value to end
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

    public static Card[] randomCards(int length){
        Random rand = new Random();
        Card[] result = new Card[length];

        int randValue = 0;
        int randColor = 0;
        int randSign = 0;

        boolean isPositive = true;
        String color = "";

        int chance = 0;
        for(int i = 0; i<length; i++){
            randValue = rand.nextInt(1, 7);
            randColor = rand.nextInt(1, 5);
            randSign = rand.nextInt(1, 3);
            switch(randSign){
                case 1:
                    isPositive = true;
                    break;
                case 2:
                    isPositive = false;
                    break;
            }
            switch(randColor){
                case 1:
                    color = "Blue";
                    break;
                case 2:
                    color = "Yellow";
                    break;
                case 3:
                    color = "Red";
                    break;
                case 4:
                    color = "Green";
                    break;
            }
            if(i<3){
                result[i] = new Card(randValue, color, isPositive, false);
            }
            else{
                chance = rand.nextInt(1, 6);
                switch(chance){
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        result[i] = new Card(randValue, color, isPositive, false);
                        break;
                    case 5:
                        result[i] = new Card(0, "none", true, true);
                        break;
                }
            }
        }
        return result;
    }

    public static void main(String[] args){
        Card[] gameDeck = DeckMaker(40);
        Card[] shuffledDeck = shuffle(gameDeck);



        // giving the first 5 cards to each player
        Card[] playerDeck = new Card[10];
        Card[] computerDeck = new Card[10];
        for(int i = 0; i<(playerDeck.length/2); i++){
            playerDeck[i] = shuffledDeck[i];
            computerDeck[i] = shuffledDeck[(shuffledDeck.length-1)-i];
        }
        
        /*
        // To check if the cards are given to players properly
        System.out.println("Shuffled Deck:");
        for(int i = 0; i<shuffledDeck.length; i++){
            System.out.println(shuffledDeck[i].value() + " " + shuffledDeck[i].color());
        }
        for(int i = 0; i<(playerDeck.length/2); i++){
            String player = String.format("%d %s", playerDeck[i].value(), playerDeck[i].color());
            String computer = String.format("%d %s", computerDeck[i].value(), computerDeck[i].color());
            System.out.println("playerDeck: " + player + " computerDeck: " + computer);
        }
        */

        // giving random cards to players
        Card[] randoms1 = randomCards(5);
        for(int i = 5; i<playerDeck.length; i++){
            playerDeck[i] = randoms1[i-5];
        }
        Card[] randoms2 = randomCards(5);
        for(int i = 5; i<computerDeck.length; i++){
            computerDeck[i] = randoms2[i-5];
        }

        System.out.println("Player hands:");
        String player = "";
        String computer = "";
        for(int i = 0; i<playerDeck.length; i++){
            if(playerDeck[i].getCardType() == "normal"){
                player = String.format("%d %s", playerDeck[i].value(), playerDeck[i].color());
            }
            else if(playerDeck[i].getCardType() == "double"){
                player = "x2";
            }
            else if(playerDeck[i].getCardType() == "flip"){
                player = "+/-";
            }

            if(computerDeck[i].getCardType() == "normal"){
                computer = String.format("%d %s", computerDeck[i].value(), computerDeck[i].color());
            }
            else if(computerDeck[i].getCardType() == "double"){
                computer = "x2";
            }
            else if(computerDeck[i].getCardType() == "flip"){
                computer = "+/-";
            }
            System.out.println("playerDeck: " + player + " computerDeck: " + computer);
        }
    }
}