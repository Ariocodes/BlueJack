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
            randValue = rand.nextInt(1, 7); // for value
            randColor = rand.nextInt(1, 5); // for color
            randSign = rand.nextInt(1, 3);  // for sign
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
                        result[i] = new Card(randValue, color, isPositive, false); // normal card
                        break;
                    case 5:
                        result[i] = new Card(0, "none", true, true); // luckyCard
                        break;
                }
            }
        }
        return result;
    }

    public static Card[] playerCardPicker(Card[] cardList){
        // chooses four cards for player hands
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
        // saving colors like so:
        // NOTE: colors don't work on windows' CMD.
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
        else if(card.getCardType() == "flip"){
            text = ANSI_PURPLE + "  [+/-] " + ANSI_RESET;
        }
        else if(card.getCardType() == "double"){
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
        // the scoreboard is ready to be saved
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
            // tries to open the file and read it
            reader = new Scanner(Paths.get("Scores.txt"));
            while(reader.hasNextLine()){
                readingList[numberOfLines] = reader.nextLine();
                numberOfLines++; // keeping the number of lines
            }
            reader = new Scanner(Paths.get("Scores.txt")); // to start from the first line again
            overload = numberOfLines - 9; // keeping the amount of overload if the amount of lines are more than 10 (numberOfLines == 9 is actually 10 lines)
            if(overload>0){ // if there is more than 10 lines
                readingList[numberOfLines] = scoreboard; // 11th index 12th value
                for(int i = 0; i<10; i++){
                    // starting to save each line starting from the overload index
                    scoresList[i] = readingList[overload+i]; // indexes: 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
                }
                f = new Formatter("Scores.txt"); // openning the file to write
                for(int i = 0; i<10; i++){
                    f.format(scoresList[i] + "\n"); // writing each score in seperate lines (separated with \n)
                }
            }
            else if(numberOfLines<=10){ // if there is less than 10 lines
                readingList[numberOfLines] = scoreboard;
                f = new Formatter("Scores.txt");
                for(int i = 0; i<numberOfLines+1; i++){
                    System.out.println(readingList[i]);
                    f.format(readingList[i] + "\n");
                }
            }
        }catch(IOException e){ // if file is not found
            e.printStackTrace();
        }finally{ // closig  reader and writer after we are done
            if(reader != null){
                reader.close();
            }
            if(f != null){
                f.close();
            }
        }
    }

    public static Card[] handOrderer(Card[] hand){
        Card temp = null;
        Card[] result = hand;
        for(int j = 0; j<result.length-1; j++){
            boolean swapped = false;
            for(int i = 0; i<result.length-1; i++){
                if(result[i].value() < result[i+1].value()){
                    temp = result[i+1];
                    result[i+1] = result[i];
                    result[i] = temp;
                    swapped = true;
                }
            }
            if(!swapped) break;
        }
        return result;
    }

    public static void gameStarter(String playerN){
        /*
        This function makes the main deck, shuffles it, makes the 5 random cards,
        mixes them and then gives 4 ordered cards to each player and keeps on restarting
        the game until player or computer has reached the score of 3. Then it 
        calls the scoreOutput function.
        */
        int[] scores = {0, 0};
        // index 0 for Player, index 1 for Computer.
        while(!(scores[0] == 3 || scores[1] == 3)){ // unless player or computer scores are not equal to 3
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
            Card[] playerDeck = handOrderer(playerCardPicker(playerPreDeck));
            Card[] computerDeck = handOrderer(playerCardPicker(computerPreDeck));
            // starting the gameplay
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

        String computerBoard = "Computer's board:           | ";
        String boardNa = playerName + "'s board:";
        String playerBoard = String.format("%1$-28s| ", boardNa);
        int amountOfCardsOnPlayerBoard = 0; // to keep track of cards on boards
        int amountOfCardsOnComputerBoard = 0;
        int deckIndex = 29;

        boolean playerDone = false; // for skip
        boolean playerFullyDone = false; // for stand
        boolean playerIsAllBlue = true; // if all cards on board are blue
        
        boolean computerDone = false;
        boolean computerFullyDone = false;
        boolean computerIsAllBlue = true;
        int computersChosenCard = 0;
        int computerDoubleCardIndex = 0;
        int computerFlipCardIndex = 0;
        boolean computerHasDouble = false;
        boolean computerHasFlip = false;

        int onlyHandOrder = 3;
        boolean computerOnlyHand = false;
        int combination = 0;
        String trick = "none";
        boolean doTrick = false;
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
                    int action = 0;
                    System.out.print(playerName + ": 1.draw, 2.card, 3.stand, 4.skip turn (1-4): ");
                    while(action != 1 && action != 2 && action != 3 && action != 4){
                        String act = sc.nextLine();
                        if(act.equals("1") || act.equals("2") || act.equals("3") || act.equals("4")){
                            try{
                                action = Integer.parseInt(act);
                            }catch(NumberFormatException e){
                                System.out.println("Wrong input. ");
                                System.out.print(playerName + ": 1.draw, 2.card, 3.stand, 4.skip turn (1-4): ");
                            }
                        }
                        else{
                            System.out.println("Wrong input. ");
                            System.out.print(playerName + ": 1.draw, 2.card, 3.stand, 4.skip turn (1-4): ");
                        }
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
                        System.out.print("Select card (1-4): ");
                        int chosenCard = 0;
                        while(chosenCard != 1 && chosenCard != 2 && chosenCard != 3 && chosenCard != 4){
                            String cd = sc.nextLine();
                            if(cd.equals("1") || cd.equals("2") || cd.equals("3") || cd.equals("4")){
                                try{
                                    chosenCard = Integer.parseInt(cd);
                                    while(player.hand[chosenCard-1].getCardType() == "none"){// making sure the card is not used before
                                        System.out.println("Wrong input.");
                                        System.out.print("Select card (1-4): ");
                                        cd = sc.nextLine();
                                        if(cd.equals("1") || cd.equals("2") || cd.equals("3") || cd.equals("4")){
                                            chosenCard = Integer.parseInt(cd);
                                        }
                                    }
                                }catch(NumberFormatException e){
                                    System.out.println("Wrong input. ");
                                    System.out.print("Select card (1-4): ");
                                }
                            }
                            else{
                                System.out.println("Wrong input. ");
                                System.out.print("Select card (1-4): ");
                            }
                        }
                        playerBoard += cardPrinter(player.hand[chosenCard-1]);
                        player.useCard(chosenCard - 1);
                        if(player.lastUsedCard.color() != "none" && player.lastUsedCard.color() != "Blue"){
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
            }// end of player's loop
            //Drawing the board
            System.out.println();
            System.out.println(computer.printHand(score[1]));
            System.out.println(computerBoard);
            System.out.println(playerBoard);
            System.out.println(player.printHand(score[0]));
            if(!computerFullyDone){ 
                // acknowledging if it has any lucky card
                for(int i = 0; i<computer.hand.length; i++){
                    if(computer.hand[i].getCardType() == "double"){
                        computerHasDouble = true;
                        computerDoubleCardIndex = i;
                    }
                    if(computer.hand[i].getCardType() == "flip"){
                        computerHasFlip = true;
                        computerFlipCardIndex = i;
                    }
                }
                while(!computerDone){
                    // asking for action
                    System.out.print("Computer: 1.draw, 2.card, 3.stand, 4.skip turn (1-4): ");
                    int action = 0;
                    if(doTrick){
                        // the last move computer does to reach 20, doTrick becomes true after normal card is thrown in the previous round.
                        if(trick.equals("double")){
                            System.out.println("2");
                            System.out.print("Select card (1-4): ");
                            System.out.println(computerDoubleCardIndex + 1);
                            computerBoard += cardPrinter(computer.hand[computerDoubleCardIndex]);
                            computer.useCard(computerDoubleCardIndex);
                            amountOfCardsOnComputerBoard++;
                            computerDone = true;
                            break;
                        }
                        else if(trick.equals("flip")){
                            System.out.println("2");
                            System.out.print("Select card (1-4): ");
                            System.out.println(computerFlipCardIndex + 1);
                            computerBoard += cardPrinter(computer.hand[computerFlipCardIndex]);
                            computer.useCard(computerFlipCardIndex);
                            amountOfCardsOnComputerBoard++;
                            computerDone = true;
                            break;
                        }
                        // the whole comuter loop will end after the LuckyCard's been used
                    }
                    for(int j = 0; j<computer.hand.length; j++){
                        // determining whether a normal card + luckyCard in hand can reach 20
                        if(computerHasDouble && computer.hand[j].value() * 2 + computer.totalValue == 20){
                            computersChosenCard = j;
                            trick = "double";
                        }
                        else if(computerHasFlip && computer.totalValue - computer.hand[j].value() == 20 && computer.totalValue - computer.hand[j].value() <= 20){
                            computersChosenCard = j;
                            trick = "flip";
                        }
                    }
                    if(trick.equals("none")){
                        /* (if normal + luckyCard trick doesn't work)
                        This section is to determine whether it can win using only its hand cards or not (all combinations are checked).
                        last card's value shouldn't be 0 because after each use the last card is 0 so the combination should change for the switch(combination) section.
                        */
                        if(computer.totalValue + computer.hand[0].value() + computer.hand[1].value() + computer.hand[2].value() + computer.hand[3].value() == 20 && computer.hand[3].value() != 0){ // 1, 2, 3, 4
                            computerOnlyHand = true;
                            combination = 1;
                        }
                        else if(computer.totalValue + computer.hand[1].value() + computer.hand[2].value() + computer.hand[3].value() == 20 && computer.hand[3].value() != 0){ // 2, 3, 4
                            computerOnlyHand = true;
                            combination = 2;
                        }
                        else if(computer.totalValue + computer.hand[0].value() + computer.hand[2].value() + computer.hand[3].value() == 20 && computer.hand[3].value() != 0){ // 1, 3, 4
                            computerOnlyHand = true;
                            combination = 3;
                        }
                        else if(computer.totalValue + computer.hand[0].value() + computer.hand[1].value() + computer.hand[3].value() == 20 && computer.hand[3].value() != 0){ // 1, 2, 4
                            computerOnlyHand = true;
                            combination = 4;
                        }
                        else if(computer.totalValue + computer.hand[0].value() + computer.hand[1].value() + computer.hand[2].value() == 20 && computer.hand[2].value() != 0){ // 1, 2, 3
                            computerOnlyHand = true;
                            combination = 5;
                        }
                        else if(computer.totalValue + computer.hand[0].value() + computer.hand[1].value() == 20 && computer.hand[1].value() != 0){ // 1, 2
                            computerOnlyHand = true;
                            combination = 6;
                        }
                        else if(computer.totalValue + computer.hand[0].value() + computer.hand[2].value() == 20 && computer.hand[2].value() != 0){ // 1, 3
                            computerOnlyHand = true;
                            combination = 7;
                        }
                        else if(computer.totalValue + computer.hand[0].value() + computer.hand[3].value() == 20 && computer.hand[3].value() != 0){ // 1, 4
                            computerOnlyHand = true;
                            combination = 8;
                        }
                        else if(computer.totalValue + computer.hand[1].value() + computer.hand[2].value() == 20 && computer.hand[2].value() != 0){ // 2, 3
                            computerOnlyHand = true;
                            combination = 9;
                        }
                        else if(computer.totalValue + computer.hand[1].value() + computer.hand[3].value() == 20 && computer.hand[3].value() != 0){ // 2, 4
                            computerOnlyHand = true;
                            combination = 10;
                        }
                        else if(computer.totalValue + computer.hand[2].value() + computer.hand[3].value() == 20 && computer.hand[3].value() != 0){ // 3, 4
                            computerOnlyHand = true;
                            combination = 11;
                        }
                        else{
                            computerOnlyHand = false;
                            combination = 0;
                        }
                        if(combination != 0) action = 2;
                        if(computerOnlyHand){ // if it can reach 20 using only the cards in its hand
                            switch(combination){ // throws last card which works 
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 8:
                                case 10:
                                case 11:
                                    computersChosenCard = 3;
                                    break;
                                case 5:
                                case 7:
                                case 9:
                                    computersChosenCard = 2;
                                    break;
                                case 6:
                                    computersChosenCard = 1;
                                    break;
                            }
                            System.out.println(action);
                        }
                        if(!computerOnlyHand){ // if no combination works
                            if(computer.totalValue < 10){ // if total is bellow 10
                                action = 1;
                                System.out.println(action);
                            }
                            boolean canComplete = false;
                            if(computer.totalValue >= 10){ // if total is 10 or higher
                                for(int i = 0; i<computer.hand.length; i++){
                                    if((computer.hand[i].value() + computer.totalValue) == 20){ // if it can reach 20 with any card from hand
                                        action = 2;
                                        canComplete = true;
                                        computersChosenCard = i;
                                        break;
                                    }
                                }
                                if(computerHasDouble && computer.totalValue + computer.lastUsedCard.value() == 20){ // if it can reach 20 with its double card
                                    action = 2;
                                    canComplete = true;
                                    computersChosenCard = computerDoubleCardIndex;
                                }
                                else if(computerHasFlip && computer.totalValue - (2 * computer.lastUsedCard.value()) == 20){ // if it can reach 20 with its flip card
                                    action = 2;
                                    canComplete = true;
                                    computersChosenCard = computerFlipCardIndex;
                                }
                                if(!canComplete){
                                    boolean couldSet = false;
                                    if(computer.totalValue >= 15){
                                        for(int i = 0; i<computer.hand.length; i++){
                                            if(computer.hand[i].getCardType() != "none" && computer.hand[i].value() > 0 && computer.hand[i].value() + computer.totalValue <= 20){
                                                // made this work only when the card is positive. can make it use either positive or negative until it can reach 20
                                                computersChosenCard = i;
                                                action = 2;
                                                couldSet = true;
                                            }
                                        }
                                    }
                                    if(computer.totalValue >= 15 && !couldSet){ // if it couldn't set
                                        Random rand = new Random();
                                        int act = rand.nextInt(0,2);
                                        switch(act){
                                            case 0:
                                                action = 1;
                                                break;
                                            case 1:
                                                action = 4;
                                                break;
                                        }
                                        couldSet = true;
                                    }
                                    else if(computer.totalValue < 15 && !couldSet){
                                        for(int i = 0; i<computer.hand.length; i++){
                                            if(computer.hand[i].getCardType() != "none" && computer.hand[i].value() > 0 && computer.hand[i].value() + computer.totalValue <= 20){
                                                // made this work only when the card is positive. can make it use either positive or negative until it can reach 20
                                                computersChosenCard = i;
                                                action = 2;
                                                couldSet = true;
                                            }
                                        }
                                        if(!couldSet){
                                            action = 1;
                                            couldSet = true;
                                        }
                                    }
                                    couldSet = false;
                                }
                                System.out.println(action);
                            }
                        }
                    }
                    else if(trick.equals("double") || trick.equals("flip")){
                        System.out.print("Select card (1-4): ");
                        System.out.println(computersChosenCard + 1);
                        computerBoard += cardPrinter(computer.hand[computersChosenCard]);
                        computer.useCard(computersChosenCard);
                        amountOfCardsOnComputerBoard++;
                        computerDone = true;
                        doTrick = true;
                        break;
                    }
                    if(action == 1){ // draw
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
                        amountOfCardsOnComputerBoard++;
                        computerDone = true;
                    }
                    else if(action == 2){ // Hand
                        System.out.print("Select card (1-4): ");
                        System.out.println(computersChosenCard + 1);
                        computerBoard += cardPrinter(computer.hand[computersChosenCard]);
                        computer.useCard(computersChosenCard);
                        if(computer.lastUsedCard.color() != "none" && computer.lastUsedCard.color() != "Blue"){
                            computerIsAllBlue = false;
                        }
                        amountOfCardsOnComputerBoard++;
                        computerDone = true;
                    }
                    else if(action == 3){ // Stand
                        // breaks out and stays out of loop until the game is over
                        computerFullyDone = true;
                        computerDone = true;
                    }
                    else if(action == 4){ // skip turn
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
            // determining winner if there is any
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
        }// end of computer's loop
        // resetting lastUsed cards
        computer.lastUsedCard = new Card(0, "none", true, false);
        player.lastUsedCard = new Card(0, "none", true, false);
        return whoWon;
    }

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