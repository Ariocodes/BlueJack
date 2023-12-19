import java.util.Random;
import java.util.Scanner;

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

    public static String cardPrinter(Card card){
        String ANSI_RESET = "\u001B[0m";
        String ANSI_BLUE = "\u001B[36m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_PURPLE = "\u001B[35m";
        String text = "";
        if(card.color() == "Blue"){
            text = String.format(ANSI_BLUE + " [B%3d] " + ANSI_RESET, card.value());
        }
        else if(card.color() == "Yellow"){
            text = String.format(ANSI_YELLOW + " [Y%3d] " + ANSI_RESET, card.value());
        }
        else if(card.color() == "Red"){
            text = String.format(ANSI_RED + " [R%3d] " + ANSI_RESET, card.value());
        }
        else if(card.color() == "Green"){
            text = String.format(ANSI_GREEN + " [G%3d] " + ANSI_RESET, card.value());
        }
        if(card.getCardType() == "flip"){
            text = ANSI_PURPLE + "  [+/-] " + ANSI_RESET;
        }
        if(card.getCardType() == "double"){
            text = ANSI_PURPLE + "  [x2]  " + ANSI_RESET;
        }
        return text;
    }

    public static void gameplay(Card[] mainDeck, Card[] playerDeck, Card[] computerDeck){
        Scanner sc = new Scanner(System.in);
        Random rd = new Random();
        Player player = new Player(playerDeck);
        Computer computer = new Computer(computerDeck);

        System.out.print("Press Enter to start!");
        String started = sc.nextLine();
        
        boolean winner = false;
        boolean winnerIsPlayer = false;
        int[] scores = {0, 0};

        String computerBoard = "Computer's board: | ";
        String playerBoard = "Player's board:   | ";
        int deckIndex = 29;
        boolean playerDone = false;
        boolean playerFullyDone = false;
        boolean computerFullyDone = false;
        boolean computerDone = false;
        while(!winner){
            //Drawing the board
            System.out.println();
            System.out.println(computer.printHand());
            System.out.println(computerBoard);
            System.out.println(playerBoard);
            System.out.println(player.printHand());
            if(!playerFullyDone){
                while(!playerDone){
                    // asking for action
                    System.out.print("Player: 1.draw, 2.card, 3.stand, 4.end turn: ");
                    int action = sc.nextInt();
                    if(action == 1){
                        //adding the card to the board first
                        playerBoard += cardPrinter(mainDeck[deckIndex]);
                        //adding its value and then setting it as the last used card
                        player.totalValue += mainDeck[deckIndex].value();
                        player.lastUsedCard = mainDeck[deckIndex];
                        mainDeck[deckIndex] = null;
                        deckIndex--; // since we are drawing cards
                        playerDone = true;
                    }
                    if(action == 2){
                        int chosenCard = 0;
                        do{
                        System.out.print("Select card (1-4): ");
                        chosenCard = sc.nextInt();
                        } while(player.hand[chosenCard-1] == null); // making sure the card is not used before

                        playerBoard += cardPrinter(player.hand[chosenCard-1]);
                        player.useCard(chosenCard);
                        playerDone = true;
                    }
                    if(action == 3){
                        // breaks out and stays out of loop until the game is over
                        playerDone = true;
                        playerFullyDone = true;
                    }
                    if(action == 4){
                        // ends the loop until the main loop iterates
                        playerDone = true;
                    }
                }
                playerDone = false;
            }
            //Drawing the board
            System.out.println();
            System.out.println(computer.printHand());
            System.out.println(computerBoard);
            System.out.println(playerBoard);
            System.out.println(player.printHand());
            if(!computerFullyDone){
                while(!computerDone){
                    // asking for action
                    System.out.print("Computer: 1.draw, 2.card, 3.stand, 4.end turn: ");
                    int action = sc.nextInt();
                    if(action == 1){
                        //adding the card to the board first
                        computerBoard += cardPrinter(mainDeck[deckIndex]);
                        //adding its value and then setting it as the last used card
                        computer.totalValue += mainDeck[deckIndex].value();
                        computer.lastUsedCard = mainDeck[deckIndex];
                        mainDeck[deckIndex] = null;
                        deckIndex--; // since we are drawing cards
                        computerDone = true;
                    }
                    if(action == 2){
                        int chosenCard = 0;
                        do{
                        System.out.print("Select card (1-4): ");
                        chosenCard = sc.nextInt();
                        } while(computer.hand[chosenCard-1] == null); // making sure the card is not used before

                        computerBoard += cardPrinter(computer.hand[chosenCard-1]);
                        computer.useCard(chosenCard);
                        computerDone = true;
                    }
                    if(action == 3){
                        // breaks out and stays out of loop until the game is over
                        computerDone = true;
                        computerFullyDone = true;
                    }
                    if(action == 4){
                        // ends the loop until the main loop iterates
                        computerDone = true;
                    }
                }
                computerDone = false;
            }
        }
    }

    public static void gameStarter(){
        Card[] gameDeck = DeckMaker(40);
        Card[] shuffledDeck = shuffle(gameDeck);
        // giving the first 5 cards to each player
        Card[] playerPreDeck = new Card[10];
        Card[] computerPreDeck = new Card[10];
        for(int i = 0; i<(playerPreDeck.length/2); i++){
            playerPreDeck[i] = shuffledDeck[i];
            computerPreDeck[i] = shuffledDeck[(shuffledDeck.length-1)-i];
            // setting the taken index as null
            shuffledDeck[i] = null;
            shuffledDeck[(shuffledDeck.length-1)-i] = null;
        }
        
        // Removing null cards
        Card[] finalDeck = new Card[30];
        int indexx = 0;
        for(int i = 0; i<shuffledDeck.length; i++){
            if(shuffledDeck[i] != null){
                finalDeck[indexx] = shuffledDeck[i];
                indexx++;
            }
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
        // Picking hand cards for each player
        Card[] playerDeck = playerCardPicker(playerPreDeck);
        Card[] computerDeck = playerCardPicker(computerPreDeck);

        gameplay(finalDeck, playerDeck, computerDeck);
    }


    public static void main(String[] args){
        gameStarter();
    }
}