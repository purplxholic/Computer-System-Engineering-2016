import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MeanThread {	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
//		 
		// TODO: read data from external file and store it in an array
		       // Note: you should pass the file as a first command line argument at runtime. 
//		Path textFilepath = Paths.get("/home/zanette/workspace/Lab 2/input.txt", args);
		FileReader textFilepath = new FileReader("/home/zanette/workspace/Lab 2/input.txt");
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
		
//		System.out.println("no.of lines:" + textLines); //test if lines work

		
		// define number of threads
		int NumOfThread = Integer.valueOf(2048);// this way, you can pass number of threads as 
		     // a second command line argument at runtime.
		
		// TODO: partition the array list into N subArrays, where N is the number of threads
		int totalInArray = textLines/ NumOfThread;
	
		List<List<Integer>> holder = new ArrayList<>(); // to hold the sub arrays
		
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
		System.out.println(holder.get(0).size());
		
		
		// TODO: start recording time
		long startTime = System.currentTimeMillis(); // start if the stopwatch  
		
		// TODO: create N threads and assign subArrays to the threads so that each thread computes mean of 
		    // its repective subarray. For example,
		//for N = 1 
		double globalmean = 0;
		ArrayList<Double> tempStorageOfMean = new ArrayList<>(); // new array mentioned above 
		ArrayList<MeanMultiThread> threads = new ArrayList<>();
		for (int k =0;k<holder.size();k++){
			
				MeanMultiThread thread = new MeanMultiThread(holder.get(k),k);
				threads.add(thread);
			
		}	
		//to start the threads
		for (MeanMultiThread multi: threads){
			multi.start();
		}
		//to wait for the threads to stop so as to get the mean 
		for (MeanMultiThread multi2:threads){
			multi2.join();
		}
		
		
		
//		subArray1 = allNumbers; 
		
//		MeanMultiThread thread1 = new MeanMultiThread(subArray1,2);
//		MeanMultiThread threadn = new MeanMultiThread(subArrayn);
		//Tip: you can't create big number of threads in the above way. So, create an array list of threads. 
		
		// TODO: start each thread to execute your computeMean() function defined under the run() method
		   //so that the N mean values can be computed. for example, 
		
//		thread1.start(); //start thread1 on from run() function
//		threadn.start();//start thread2 on from run() function

		 //join() then getMean because the mean is calculated 
		for (MeanMultiThread multi3: threads){
			tempStorageOfMean.add(multi3.getMean());
		}
								
		
		
		
//		thread1.join();//wait until thread1 terminates
//		threadn.join();//wait until threadn terminates
		
		// TODO: show the N mean values

		for (Double ddouble:tempStorageOfMean){
			System.out.println("Temporal mean value of thread "+ tempStorageOfMean.indexOf(ddouble) +" is ..." + ddouble);
		}
		
		
		// TODO: store the temporal mean values in a new array so that you can use that 
		    /// array to compute the global mean.
		
		tempStorageOfMean.add(globalmean);
		
		// TODO: compute the global mean value from N mean values. 
		double globalMean = 0 ;
		double globalSum =0;
		if (tempStorageOfMean.size() > 1){
			for (int i1 =0; i1<tempStorageOfMean.size();i1++){
				globalSum += tempStorageOfMean.get(i1);
			}
			globalMean = globalSum / NumOfThread; 
		}
		else{
			globalMean = globalmean;
		}
		System.out.println(globalMean);
		
		// TODO: stop recording time and compute the elapsed time 
		long stopTiming = System.currentTimeMillis();
		long totalTime = stopTiming - startTime; //will get in ms 
		System.out.println("The total time taken is: " + (totalTime) +"ms");
		System.out.printf("The global mean value is ... %.3f ", globalMean );
				
	}
}

//Extend the Thread class
class MeanMultiThread extends Thread {
	private List<Integer> list;
	private double mean;
//	private int threadnumber
	MeanMultiThread(List<Integer> array,int threadNumber) {
		list = array;
		System.out.println("Creating Thread " + threadNumber);
		
	}
	public double getMean() {
		return this.mean;
	}
	public void run() {
		// TODO: implement your actions here, e.g., computeMean(...)
		computeMean(list);
	}

	public void computeMean(List<Integer> List){
		double temp = 0;
		
//		System.out.println("Calculating...");
		for (int i=0;i<List.size();i++){
			temp = temp + List.get(i);
//			System.out.println(temp);
		}
//		System.out.println(temp);
		this.mean = temp / List.size();
//		System.out.println(this.mean);
		
		
	}
}
