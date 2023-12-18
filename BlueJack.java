import java.util.Random;

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

    public static Card[] playerCardPicker(Card[] cardList){
        Random rand = new Random();
        Card[] resultArray = new Card[4];
        
        int[] usedIndexes = {10, 10, 10, 10}; // preset as 10 so the algorithm doesn't bug out (normally preset as 0 and it collides with index0).
        boolean set = true;
        int randomIndex = 0;
        int numOfSets = 0; // to keep track of the amount of successful sets.
        while(numOfSets<4){
            set = true;
            randomIndex = rand.nextInt(0, 10);
            for(int j: usedIndexes){
                if(randomIndex == j){ // set unless the random index has been used.
                    set = false;
                    break;
                }
            }
            if(set){
                resultArray[numOfSets] = cardList[randomIndex];
                usedIndexes[numOfSets] = randomIndex;
                numOfSets++;
            }
            
        }
        return resultArray;
    }

    public static void pre_printer(Card[] deckOne, Card[] deckTwo){
        // Basic function for troubleshooting (a basic printer)
        String one = "";
        String two = "";
        for(int i = 0; i<deckOne.length; i++){
            if(deckOne[i].getCardType() == "normal"){
                one = String.format("%d %s", deckOne[i].value(), deckOne[i].color());
            }
            else if(deckOne[i].getCardType() == "double"){
                one = "x2";
            }
            else if(deckOne[i].getCardType() == "flip"){
                one = "+/-";
            }

            if(deckTwo[i].getCardType() == "normal"){
                two = String.format("%d %s", deckTwo[i].value(), deckTwo[i].color());
            }
            else if(deckTwo[i].getCardType() == "double"){
                two = "x2";
            }
            else if(deckTwo[i].getCardType() == "flip"){
                two = "+/-";
            }
            System.out.println((i+1) + ". deckOne: " + one + "   deckTwo: " + two);
        }
    }

    public static void main(String[] args){
        Card[] gameDeck = DeckMaker(40);
        Card[] shuffledDeck = shuffle(gameDeck);

        // giving the first 5 cards to each player
        Card[] playerPreDeck = new Card[10];
        Card[] computerPreDeck = new Card[10];
        for(int i = 0; i<(playerPreDeck.length/2); i++){
            playerPreDeck[i] = shuffledDeck[i];
            computerPreDeck[i] = shuffledDeck[(shuffledDeck.length-1)-i];
        }

        // giving random cards to PreDecks
        Card[] randoms1 = randomCards(5);
        for(int i = 5; i<playerPreDeck.length; i++){
            playerPreDeck[i] = randoms1[i-5];
        }
        Card[] randoms2 = randomCards(5);
        for(int i = 5; i<computerPreDeck.length; i++){
            computerPreDeck[i] = randoms2[i-5];
        }

        Card[] playerDeck = playerCardPicker(playerPreDeck);
        Card[] computerDeck = playerCardPicker(computerPreDeck);
        pre_printer(playerDeck, computerDeck);
    }
}