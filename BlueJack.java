import java.util.Random;
import java.util.Scanner;
import java.lang.Math;
import java.nio.file.Paths;
import java.util.Formatter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
                chance = rand.nextInt(1,6);
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
    
    public static void scoreOutput(String playerN, int[] scores){
        /*
        This function is used to print the scores and the winner int the consol,
        and save the scores in the text file called "Scores.txt"
        */
        LocalDateTime dt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
        String formattedDate = dt.format(formatter);

        String scoreboard = playerN + ": " + scores[0] + "  |  Computer: " + scores[1] + " | " + formattedDate;
        if(scores[0] == 3){
            System.out.println(playerN + " won the game!");
            System.out.println(scoreboard);
        }
        else if(scores[1] == 3){
            System.out.println("Computer won the game!");
            System.out.println(scoreboard);
        }

        Scanner reader = null;
        Formatter f = null;
        int numberOfLines = 0;
        int overload = 0;
        String[] scoresList = new String[10];
        String[] readingList = new String[20];
        try{
            reader = new Scanner(Paths.get("Scores.txt"));
            while(reader.hasNextLine()){
                readingList[numberOfLines] = reader.nextLine();
                numberOfLines++;
            }
            reader = new Scanner(Paths.get("Scores.txt")); // to start from the first line again
            overload = numberOfLines - 9;
            if(overload>0){
                readingList[numberOfLines] = scoreboard; // 11th index 12th value
                for(int i = 0; i<10; i++){
                    scoresList[i] = readingList[overload+i]; // indexes: 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
                }
                f = new Formatter("Scores.txt");
                for(int i = 0; i<10; i++){
                    f.format(scoresList[i] + "\n");
                }
            }
            else if(numberOfLines<=10){
                readingList[numberOfLines] = scoreboard;
                f = new Formatter("Scores.txt");
                for(int i = 0; i<numberOfLines+1; i++){
                    System.out.println(readingList[i]);
                    f.format(readingList[i] + "\n");
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                reader.close();
            }
            if(f != null){
                f.close();
            }
        }
    }

    public static void gameStarter(String playerN){
        /*
        This function makes the main deck, shuffles it, makes the 5 random cards,
        mixes them and then gives 4 cards to each player and keeps on restarting
        the game until player or computer has reached the score of 3. Then it 
        calls the scoreOutput function.
        */
        Card[] gameDeck = DeckMaker(40);
        Card[] shuffledDeck = shuffle(gameDeck);
        // giving the first 5 cards to each player
        Card[] playerPreDeck = new Card[10];
        Card[] computerPreDeck = new Card[10];
        for(int i = 0; i<(playerPreDeck.length/2); i++){
            playerPreDeck[i] = shuffledDeck[i];
            computerPreDeck[i] = shuffledDeck[(shuffledDeck.length-1)-i];
            // setting the taken indexes as null
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
        int[] scores = {0, 0};
        // index 0 for Player, index 1 for Computer.
        while(!(scores[0] == 3 || scores[1] == 3)){ // unless player or computer scores are not equal to 3
            String status = gameplay(finalDeck, playerDeck, computerDeck, playerN, scores);
            if(status == "Player"){ // if winner is player
                scores[0]++;
            }
            else if(status == "Computer"){ // if winner is computer
                scores[1]++;
            }
            else if(status == "PlayerACE"){
                scores[0] = 3;
            }
            else if(status == "ComputerACE"){
                scores[1] = 3;
            }
            gameDeck = DeckMaker(40);
            shuffledDeck = shuffle(gameDeck);
            // giving the first 5 cards to each player
            playerPreDeck = new Card[10];
            computerPreDeck = new Card[10];
            for(int i = 0; i<(playerPreDeck.length/2); i++){
                playerPreDeck[i] = shuffledDeck[i];
                computerPreDeck[i] = shuffledDeck[(shuffledDeck.length-1)-i];
                // setting the taken indexes as null
                shuffledDeck[i] = null;
                shuffledDeck[(shuffledDeck.length-1)-i] = null;
            }
            // Removing null cards
            finalDeck = new Card[30];
            indexx = 0;
            for(int i = 0; i<shuffledDeck.length; i++){
                if(shuffledDeck[i] != null){
                    finalDeck[indexx] = shuffledDeck[i];
                    indexx++;
                }
            }
            // giving random cards to PreDecks
            randoms1 = randomCards(5);
            for(int i = 5; i<playerPreDeck.length; i++){
                playerPreDeck[i] = randoms1[i-5];
            }
            randoms2 = randomCards(5);
            for(int i = 5; i<computerPreDeck.length; i++){
                computerPreDeck[i] = randoms2[i-5];
            }
            // Picking hand cards for each player
            playerDeck = playerCardPicker(playerPreDeck);
            computerDeck = playerCardPicker(computerPreDeck);
        }
        scoreOutput(playerN, scores);
    }

    public static String gameplay(Card[] mainDeck, Card[] playerDeck, Card[] computerDeck, String playerName, int[] score){
        /*
        This function consists of the whole gameplay (printing the board, asking user for action,
        and deciding on who has won)
        */
        Scanner sc = new Scanner(System.in);
        Random rd = new Random();
        Player player = new Player(playerDeck, playerName);
        Computer computer = new Computer(computerDeck);

        System.out.print("Press Enter to start!");
        String started = sc.nextLine();
        
        boolean winner = false;
        String whoWon = "";

        String computerBoard = "Computer's board: | ";
        String playerBoard = playerName + "'s board:   | ";
        int amountOfCardsOnPlayerBoard = 0; // to keep track of cards on boards
        int amountOfCardsOnComputerBoard = 0;
        int deckIndex = 29;

        boolean playerDone = false; // for skip
        boolean playerFullyDone = false; // for stand
        boolean playerIsAllBlue = true; // if all cards on board are blue
        
        boolean computerDone = false;
        boolean computerFullyDone = false;
        boolean computerIsAllBlue = true;
        //Drawing the board
        System.out.println();
        System.out.println(computer.printHand(score[1]));
        System.out.println(computerBoard);
        System.out.println(playerBoard);
        System.out.println(player.printHand(score[0]));
        while(!winner){
            if(!playerFullyDone){
                while(!playerDone){
                    // asking for action
                    System.out.print(playerName + ": 1.draw, 2.card, 3.stand, 4.end turn (1-4): ");
                    int action = sc.nextInt();
                    while(action != 1 && action != 2 && action != 3 && action != 4){
                        System.out.print("Wrong input. ");
                        System.out.print(playerName + ": 1.draw, 2.card, 3.stand, 4.end turn (1-4): ");
                        action = sc.nextInt();
                    }
                    if(action == 1){
                        //adding the card to the board first
                        playerBoard += cardPrinter(mainDeck[deckIndex]);
                        //adding its value and then setting it as the last used card
                        player.totalValue += mainDeck[deckIndex].value();
                        player.lastUsedCard = mainDeck[deckIndex];
                        if(player.lastUsedCard.color() != "Blue"){
                            playerIsAllBlue = false;
                        }
                        mainDeck[deckIndex] = null;
                        deckIndex--; // since we are drawing cards
                        playerDone = true;
                        amountOfCardsOnPlayerBoard++;
                    }
                    else if(action == 2){
                        int chosenCard = 0;
                        do{
                        System.out.print("Select card (1-4): ");
                        chosenCard = sc.nextInt();
                        } while(player.hand[chosenCard-1] == null); // making sure the card is not used before

                        playerBoard += cardPrinter(player.hand[chosenCard-1]);
                        player.useCard(chosenCard);
                        if(player.lastUsedCard.color() != "Blue"){
                            playerIsAllBlue = false;
                        }
                        playerDone = true;
                        amountOfCardsOnPlayerBoard++;
                    }
                    else if(action == 3){
                        // breaks out and stays out of loop until the game is over
                        playerDone = true;
                        playerFullyDone = true;
                    }
                    else if(action == 4){
                        // ends the loop until the main loop iterates
                        playerDone = true;
                    }
                }
                playerDone = false;
            }
            //Drawing the board
            System.out.println();
            System.out.println(computer.printHand(score[1]));
            System.out.println(computerBoard);
            System.out.println(playerBoard);
            System.out.println(player.printHand(score[0]));
            if(!computerFullyDone){
                while(!computerDone){
                    // asking for action
                    System.out.print("Computer: 1.draw, 2.card, 3.stand, 4.skip turn (1-4): ");
                    int action = sc.nextInt();
                    while(action != 1 && action != 2 && action != 3 && action != 4){
                        System.out.print("Wrong input. ");
                        System.out.print("Computer: 1.draw, 2.card, 3.stand, 4.end turn (1-4): ");
                        action = sc.nextInt();
                    }
                    if(action == 1){
                        //adding the card to the board first
                        computerBoard += cardPrinter(mainDeck[deckIndex]);
                        //adding its value and then setting it as the last used card
                        computer.totalValue += mainDeck[deckIndex].value();
                        computer.lastUsedCard = mainDeck[deckIndex];
                        if(computer.lastUsedCard.color() != "Blue"){
                            computerIsAllBlue = false;
                        }
                        mainDeck[deckIndex] = null;
                        deckIndex--; // since we are drawing cards
                        computerDone = true;
                        amountOfCardsOnComputerBoard++;
                    }
                    else if(action == 2){
                        int chosenCard = 0;
                        do{
                        System.out.print("Select card (1-4): ");
                        chosenCard = sc.nextInt();
                        } while(computer.hand[chosenCard-1] == null); // making sure the card is not used before

                        computerBoard += cardPrinter(computer.hand[chosenCard-1]);
                        computer.useCard(chosenCard);
                        if(computer.lastUsedCard.color() != "Blue"){
                            computerIsAllBlue = false;
                        }
                        computerDone = true;
                        amountOfCardsOnComputerBoard++;
                    }
                    else if(action == 3){
                        // breaks out and stays out of loop until the game is over
                        computerDone = true;
                        computerFullyDone = true;
                    }
                    else if(action == 4){
                        // ends the loop until the main loop iterates
                        computerDone = true;
                    }
                }
                computerDone = false;
            }
            //Drawing the board
            System.out.println();
            System.out.println(computer.printHand(score[1]));
            System.out.println(computerBoard);
            System.out.println(playerBoard);
            System.out.println(player.printHand(score[0]));
            if(player.totalValue == 20 && computer.totalValue == 20){
                System.out.println("Tie!");
                winner = true;
                whoWon = "none";
            }
            else if(player.totalValue == 20){
                if(playerIsAllBlue){
                    System.out.println("Player Aced!");
                    whoWon = "PlayerACE";
                }
                else{
                    System.out.println(playerName + " wins!");
                    winner = true;
                    whoWon = "Player";
                }
            }
            else if(computer.totalValue == 20){
                if(computerIsAllBlue){
                    System.out.println("Computer Aced!");
                    whoWon = "ComputerACE";
                }
                else{
                    System.out.println("Computer wins!");
                    winner = true;
                    whoWon = "Computer";
                }
            }
            else if(player.totalValue > 20 || computer.totalValue > 20){
                if(player.totalValue > computer.totalValue){
                    System.out.println(playerName + " busted! (total>20) Computer Wins!");
                    winner = true;
                    whoWon = "Computer";
                }
                if(computer.totalValue > player.totalValue){
                    System.out.println("Computer busted! (total>20) " + playerName + " wins!");
                    winner = true;
                    whoWon = "Player";
                }
            }
            else if(amountOfCardsOnPlayerBoard == 9){
                System.out.println(playerName + " wins! (9 cards on board and total bellow or equal to 20)");
                winner = true;
                whoWon = "Player";
            }
            else if(amountOfCardsOnComputerBoard == 9){
                System.out.println("Computer wins! (9 cards on board and total bellow or equal to 20)");
                winner = true;
                whoWon = "Computer";
            }
            else{
                if(playerFullyDone && computerFullyDone){
                    int diffPlayer = (int)Math.sqrt(Math.pow(player.totalValue - 20, 2));
                    int diffComputer = (int)Math.sqrt(Math.pow(computer.totalValue - 20, 2));
                    if(diffPlayer < diffComputer){
                        System.out.println(playerName + " wins!");
                        winner = true;
                        whoWon = "Player";
                    }
                    else if(diffComputer < diffPlayer){
                        System.out.println("Computer wins!");
                        winner = true;
                        whoWon = "Computer";
                    }
                    else if(diffComputer == diffPlayer){
                        System.out.println("Tie!");
                        winner = true;
                        whoWon = "none";
                    }
                }
            }
        }
        computer.lastUsedCard = new Card(0, "none", true, false);
        player.lastUsedCard = new Card(0, "none", true, false);
        return whoWon;
    }

    // public static void saver(){

    // }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String playerName = "Player";
        String again = "y";
        while(again.equals("Y") || again.equals("y")){
            System.out.print("Enter your name: ");
            playerName = sc.nextLine();
            gameStarter(playerName);
            System.out.println("Game over!");
            System.out.print("Want to play again?(y/n): ");
            again = sc.nextLine();
            while(!again.equals("Y") && !again.equals("y") && !again.equals("N") && !again.equals("n")){
                System.out.println("wrong input.");
                System.out.print("Want to play again?(y/n): ");
                again = sc.nextLine();
            }
        }

    }
}