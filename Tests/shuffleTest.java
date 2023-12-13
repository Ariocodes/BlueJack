import java.util.Random;

public class shuffleTest{
    public static void main(String[] args){
        int[] mainArray = {1,2,3,4,5,6,7,8,9,10};
        int[] alreadySaved = new int[mainArray.length]; // values are preset as 0
        int[] resultArray = new int[mainArray.length];
        Random rand = new Random();
        boolean save = true;
        int randomIndex = 0;
        int savingIndex = 0;

        while(savingIndex < mainArray.length){
            save = true;
            randomIndex = rand.nextInt(1, mainArray.length+1); 
            /* set a random index value (first index as 1, last as length+1)
            because there are values of 0 presaved in the array which the algorithm skips since it's treated as an already existing value.
            */
            for(int i = 0; i<alreadySaved.length; i++){ // Check if the random index value was saved before (if saved = don't save)
                if(alreadySaved[i] == randomIndex){
                    save = false;
                    break;
                }
            }
            if(save){
                alreadySaved[savingIndex] = randomIndex;
                resultArray[savingIndex] = mainArray[randomIndex-1];
                savingIndex++;
            }
            // for(int j = 0; j<){}
        }
        for(int i = 0; i<resultArray.length; i++){
            System.out.println(resultArray[i]);
        }
    }
}