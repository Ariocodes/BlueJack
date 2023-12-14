import java.util.Random;

public class shuffleTest{
    public static void main(String[] args){
        /*
        The function takes a main array of cards, uses random to select a random index and checks 
        if the index has been used before or not (previously used indexes are saved in an additional array
        so we can keep track of them.) then, if the index was not used before, it sets the value
        inside that random index of the mainarray as value inside the result array (starting from first index)
        */
        int[] mainArray = {1,2,3,4,5,6,7,8,9,10};
        int[] alreadySaved = new int[mainArray.length]; // indexes are already preset. (values are preset as 0)
        int[] resultArray = new int[mainArray.length];
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
            for(int i = 0; i<alreadySaved.length; i++){ // Checking if the random index value was saved before (if already saved = don't save)
                if(alreadySaved[i] == randomIndex){
                    save = false;
                    break;
                }
            }
            if(save){
                alreadySaved[savingIndex] = randomIndex;
                resultArray[savingIndex] = mainArray[randomIndex-1]; // -1 because we chose the random index from 1 to length+1 (index0 as 1, index1 as 2 and so on.)
                savingIndex++;
            }
            // for(int j = 0; j<){}
        }
        for(int i = 0; i<resultArray.length; i++){
            System.out.println(resultArray[i]);
        }
    }
}