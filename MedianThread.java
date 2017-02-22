



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;


public class MedianThread {
//    private static int textLines;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException  {
        // TODO: read data from external file and store it in an array
        // Note: you should pass the file as a first command line argument at runtime.
        FileReader textFilepath = new FileReader(args[0]);
        //C:\Users\The Gt Zan\Downloads\input.txt
        ArrayList<Integer> allNumbers = new ArrayList<>(); //list to contain the numbers from the text file
        int textLines = 0;

        Scanner scanner;
        scanner = new Scanner(textFilepath);
//		System.out.println(textFilepath); //test if it's working

        while (scanner.hasNextInt()){
            if (scanner.hasNextInt()){
                allNumbers.add(scanner.nextInt());
//					System.out.println(scanner.nextInt()+"\n");
                textLines ++;
            }
            else{
                scanner.next();
            }
        }
        scanner.close();

        // define number of threads
        int NumOfThread = Integer.valueOf(args[1]);// this way, you can pass number of threads as
        // a second command line argument at runtime.

        // TODO: partition the array list into N subArrays, where N is the number of threads

        int totalInArray = textLines/ NumOfThread;

        List<List<Integer>> holder2 = new ArrayList<>(); // to hold the sub arrays


        //code to create into N sub arrays
        int count = 0;
        int j =0;

        //cuont = totalinarray
        //position = 0 ;
        //i =0  < total
        while (count < NumOfThread){


            if (count == (NumOfThread)-1){
                holder2.add(allNumbers.subList(j,allNumbers.size()));
            }
            else{
                holder2.add(allNumbers.subList(j,j+totalInArray));
            }
            count++;
            j+= totalInArray;
        }


        // TODO: start recording time
        long startTime = System.currentTimeMillis(); // start if the stopwatch

        // TODO: create N threads and assign subArrays to the threads so that each thread sorts
        // its repective subarray. For example,


        //Tip: you can't create big number of threads in the above way. So, create an array list of threads.


        ArrayList<MedianMultiThread> threads2 = new ArrayList<>();
        for (int k =0;k<holder2.size();k++){

            MedianMultiThread thread = new MedianMultiThread(holder2.get(k),k);
            thread.start();
            threads2.add(thread);

        }
        // TODO: start each thread to execute your sorting algorithm defined under the run() method, for example,

        final ArrayList<Integer> ultimateArray = new ArrayList<>();
        for (MedianMultiThread multi2:threads2){
            multi2.join();

                ultimateArray.addAll(multi2.getSortedList());

        }

        // TODO: use any merge algorithm to merge the sorted subarrays and store it to another array, e.g., sortedFullArray.


        Integer[] integers = ultimateArray.toArray(new Integer[ultimateArray.size()]);
        sort(integers);
//        ArrayList<Integer> n = new ArrayList<>(ultimateArray);
        ArrayList<Integer> n = new ArrayList<>(Arrays.asList(integers));


        //TODO: get median from sortedFullArray
        double median = computeMedian(n);

        //e.g, computeMedian(sortedFullArray);

        // TODO: stop recording time and compute the elapsed time
        long stopTiming = System.currentTimeMillis();
        long runningTime = stopTiming - startTime; //will get in ms
        // TODO: printout the final sorted array
//        for (Integer i:n){
//            System.out.println(i);
//        }
        System.out.println(n);
        try{
            PrintWriter printWriter = new PrintWriter("out.txt","UTF-8");
//            for (Integer i:n){
//            printWriter.print(i + " , ");
//            }
            printWriter.println(n);
            printWriter.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        // TODO: printout median
        System.out.println("The Median value is ..." + median);
        System.out.println("Running time is " + runningTime + " milliseconds\n");

    }

    public static double computeMedian(ArrayList<Integer> inputArray) {
        //TODO: implement your function that computes median of values of an array
        int m = inputArray.size()/2;
//        System.out.println(m);
//        System.out.println(inputArray);
//        System.out.println(inputArray.size());
//        System.out.println(inputArray.get(0));
//        System.out.println(inputArray.get(m));
//        System.out.println(inputArray.get(m+1));
        return 0.5 * (inputArray.get(m)+inputArray.get(m+1));

    }
    private static Integer[] array;
    private static int length;
    private static Integer[] tempArray;
    public static void sort(Integer[] integers){
        array = integers; //copy the array over
        length = integers.length; //size of the array input
        tempArray = new Integer[length]; //creation of helper array
        mergeSort1(0,length-1); //start the merge of everything
    }

    public static void mergeSort1(int lowIndex,int highIndex){
        if (lowIndex<highIndex){
            int middleIndex = lowIndex + (highIndex-lowIndex)/2; // the middle index
            //so it would be 0 + (3-0)/2 = 1.5 = 1

            mergeSort1(lowIndex,middleIndex); //do it again for LHS
            //0 , 1 so middle index will be 1/2 = 0
            //0, 0 = 0
            mergeSort1(middleIndex+1,highIndex); //do it again for RHS
            //2,2

            mergeArrays(lowIndex,middleIndex,highIndex);
            //merge the arrays
        }
    }

    public static void mergeArrays(int lowIndex, int middleIndex, int highIndex){
        //copy over the original array to the new one
        for (int i =lowIndex;i<= highIndex;i++){
            tempArray[i] = array[i];
        }

        int i = lowIndex;
        int j = middleIndex +1;
        int k = lowIndex;

        while (i <= middleIndex && j<=highIndex){
            if (tempArray[i] <= tempArray[j]){
                array[k] = tempArray[i];
                i++;
            }
            else{
                array[k] = tempArray[j];
                j++;
            }
            k++;
        }
        while (i<=middleIndex){
            array[k] = tempArray[i];
            k++;
            i++;
        }
    }





}

// extend Thread
class MedianMultiThread extends Thread {
    private ArrayList<Integer> list;
    private ArrayList<Integer> returnList;
    private Integer[] test;

    public ArrayList<Integer> getSortedList() {

        return returnList;
    }

    MedianMultiThread(List<Integer> array,int k) {
        this.list = new ArrayList<>(array);
//        System.out.println(this.list);
        //convert to Integer array
        test = array.toArray(new Integer[array.size()]);

//        returnList = new ArrayList<>();
//        System.out.println("Thread " + k + " created!");

    }

    public void run() {
        // called by object.start()

        sort(test);
        returnList = new ArrayList<Integer>(Arrays.asList(test));

//       returnList = new ArrayList<>(mergesort2(list)) ;
//        System.out.println(returnList);
            }




    ////////////////////////////////////////////////////////////////
    // TODO: implement merge sort here, recursive algorithm
    ////////////////////////////////////////////////////////////////
    private Integer[] array;
    private int length;
    private Integer[] tempArray;

    public void sort(Integer[] integers){
        this.array = integers; //copy the array over
        this.length = integers.length; //size of the array input
        this.tempArray = new Integer[length]; //creation of helper array
        mergeSort1(0,length-1); //start the merge of everything
    }

    public void mergeSort1(int lowIndex,int highIndex){
        if (lowIndex<highIndex){
            int middleIndex = lowIndex + (highIndex-lowIndex)/2; // the middle index
            //so it would be 0 + (3-0)/2 = 1.5 = 1

            mergeSort1(lowIndex,middleIndex); //do it again for LHS
            //0 , 1 so middle index will be 1/2 = 0
            //0, 0 = 0
            mergeSort1(middleIndex+1,highIndex); //do it again for RHS
            //2,2

            mergeArrays(lowIndex,middleIndex,highIndex);
            //merge the arrays
        }
    }

    public void mergeArrays(int lowIndex, int middleIndex, int highIndex){
        //copy over the original array to the new one
        for (int i =lowIndex;i<= highIndex;i++){
            tempArray[i] = array[i];
        }

        int i = lowIndex;
        int j = middleIndex +1;
        int k = lowIndex;

        while (i <= middleIndex && j<=highIndex){
            if (tempArray[i] <= tempArray[j]){
                array[k] = tempArray[i];
                i++;
            }
            else{
                array[k] = tempArray[j];
                j++;
            }
            k++;
        }
        while (i<=middleIndex){
            array[k] = tempArray[i];
            k++;
            i++;
        }
    }


//    public ArrayList<Integer> mergesort2(ArrayList<Integer> integers){
//        if (integers.size()<=1){
//            return integers;
//        }
//
//        ArrayList<Integer> left = new ArrayList<>();
//        ArrayList<Integer> right = new ArrayList<>();
//
//        for (int i=0; i<integers.size();i++){
//            if (i< (integers.size())/2){
//                left.add(integers.get(i));
////                System.out.println("left"  + left);
//            }
//            else{
//                right.add(integers.get(i));
////                System.out.println("right" + right);
//            }
//        }
//
//        left = mergesort2(left);
//        right = mergesort2(right);
//
//        return merge(left,right);
//    }
//
//    public ArrayList<Integer> merge(ArrayList<Integer> l , ArrayList<Integer> r){
//        ArrayList<Integer> result = new ArrayList<>();
//
//        while (!l.isEmpty() && !r.isEmpty()){
//            if (l.get(0) <= r.get(0)){
//                result.add(l.get(0));
//                l.remove(0);
//            }
//            else{
//                result.add(r.get(0));
//                r.remove(0);
//            }
//        }
//
//        while (!l.isEmpty()){
//            result.add(l.get(0));
//            l.remove(0);
//        }
//        while (!r.isEmpty()){
//            result.add(r.get(0));
//            r.remove(0);
//        }
//        return result;
//    }



}

