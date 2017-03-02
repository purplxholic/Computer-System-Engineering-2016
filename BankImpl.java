package com.example;

public class BankImpl {
	private int numberOfCustomers;	// the number of customers
	private int numberOfResources;	// the number of resources

	private int[] available; 	// the available amount of each resource
	private int[][] maximum; 	// the maximum demand of each customer
	private int[][] allocation;	// the amount currently allocated
	private int[][] need;		// the remaining needs of each customer
	
	public BankImpl (int[] resources, int numberOfCustomers) {
		// TODO: set the number of resources
		this.numberOfResources = resources.length;
		// TODO: set the number of customers
		this.numberOfCustomers = numberOfCustomers;
		// TODO: set the value of bank resources to available
		this.available = resources;
		// TODO: set the array size for maximum, allocation, and need
		need = new int[numberOfCustomers][this.numberOfResources];
		maximum = new int[numberOfCustomers][this.numberOfResources];
		allocation= new int[numberOfCustomers][this.numberOfResources];

	}
	
	public int getNumberOfCustomers() {
		// TODO: return numberOfCustomers [done] Q1
		return numberOfCustomers;
	}

	public void addCustomer(int customerNumber, int[] maximumDemand) {
		// TODO: add customer, update maximum and need Q1
//		this.numberOfCustomers += customerNumber;
//		int[][] maximum1 = {
//				{7, 5, 3},
//				{3, 2, 2},
//				{9, 0, 2},
//				{2, 2, 2},
//				{4, 3, 3},
//		};
		for (int i=0; i<numberOfResources;i++){
			//max[i,j] = k mean process i would want type j at k instances
			maximum[customerNumber][i] = maximumDemand[i];
			//need[i,j] = k means process i may need k more instances of R
			need[customerNumber][i] = maximumDemand[i];
		}
	}

	public void getState() {
		// TODO: print the current state with a tidy format Q1
		System.out.println("Current State:\n");
		// TODO: print available
		System.out.println("Available:");
		for (int avail: available){
			System.out.print(avail + " ");
		}

		System.out.println();
		// TODO: print maximum
		System.out.println("Maximum resource allocated:\n");
		int count =0;
		for (int i=0;i<maximum.length;i++){
			for (int res:maximum[i]){
				System.out.print(res + " ");
				count ++;
				if (count ==3){
					System.out.println();
					count =0;
				}
			}

		}
		System.out.println();
		// TODO: print allocation
		System.out.println("Allocated amount of resources:\n");
		for (int j=0;j<allocation.length;j++){
			for (int res:allocation[j]){
				System.out.print(res + " ");
				count ++;
				if (count ==3){
					System.out.println();
					count =0;
				}
			}
		}
		System.out.println();
		// TODO: print need
		System.out.println("Known Need");
		for (int k=0;k<need.length;k++){
			for (int res:need[k]){
				System.out.print(res + " ");
				count ++;
				if (count ==3){
					System.out.println();
					count =0;
				}
			}
		}
	}

	public synchronized boolean requestResources(int customerNumber, int[] request) {
		// TODO: Q1
		// TODO: print the request
		System.out.println("Customer " + customerNumber + " requested:");
		for (int i=0;i<request.length;i++){
			System.out.print(request[i] + " ");

		}
		System.out.println();
		// TODO: check if request larger than need
		for (int i=0;i<numberOfResources;i++){
			if (request[i] > need[customerNumber][i]){
				//If Request[i] <= Need[i] go to Step 2. Otherwise, raise error, since
				//process has exceeded its maximum claim.
				System.out.println("Error! Sorry customer " + customerNumber+ ". Request exceeds the maximum claim");
				return false;
			}
			else{
				// TODO: check if request larger than available
				//If Request[i] <= Available, go to Step 3. Otherwise Pi must wait, since
				//		the resources are not immediately available.
				if (request[i] > available[i]){
					System.out.println("Oops! There is not enough resources");
					return false;
				}
			}

		}
		// TODO: check if the state is safe or not
		if (checkSafe(customerNumber,request)) {
			System.out.println("It is safe to allocate! Thank you for banking with ISTD");
			// TODO: if it is safe, allocate the resources to customer customerNumber
			for (int i=0;i<numberOfResources;i++){
				available[i] = available[i] - request[i];
				allocation[customerNumber][i] = allocation[customerNumber][i] + request[i];
				need[customerNumber][i] = need[customerNumber][i] - request[i];
			}
		}
		else{
			System.out.println("Apologises! It isn't safe! Unable to allocate resources");
			return false;
		}


		return true;

	}

	public synchronized void releaseResources(int customerNumber, int[] release) {
		// TODO: print the release Q1
		System.out.println("Releasing resources to customer " + customerNumber);
		// release the resources from customer customerNumber
		//slide 7.24 activity 5.1
		for (int i=0;i<numberOfResources;i++){
			System.out.println("Releasing " + release[i]);
			available[i] += release[i] ; //add on to the available amount of work
			allocation[customerNumber][i] -= release[i] ; //remove the allocation
			need[customerNumber][i] += release[i];  //need = max - allocation ; customer may still need to add on
		}

	}

	private synchronized boolean checkSafe(int customerNumber, int[] request) {
		// TODO: check if the state is safe
		int[] temp_avail = new int[10]; //cos 10 is the maximum
		int[] work = temp_avail;
		int[][] temp_need = new int[10][10];
		int[][] temp_allocation = new int[10][10];

		for (int i =0; i<numberOfResources;i++){ //numofresources: you copy over the same number of types
			temp_avail[i] = available[i] - request[i];
			for (int j=0;j<numberOfCustomers;j++){ //numberofCustomers because u r doing onthe 2nd dimen of array
				if (j == customerNumber){
					temp_need[customerNumber][i] = need[customerNumber][i] - request[i];
					temp_allocation[customerNumber][i] = allocation[customerNumber][i] + request[i];
				}
				else{
					//then just need = need
					temp_need[j][i] = need[j][i];
					temp_allocation[j][i] = allocation[j][i];
				}

			}
		}

		boolean[] finish = new boolean[10];
		for (int i=0;i<finish.length;i++){
			finish[i] = false;
		}
		boolean possible = true;

		while (possible){
			possible = false;
			for (int ci =0;ci<numberOfCustomers;ci++){
				boolean checking = true; //check each value in the second
				for (int j=0;j<numberOfResources;j++){
					if (temp_need[ci][j] > work[j]){
						checking = false;
					}


				}

				if (!finish[ci] && checking ){
					for (int j=0;j<numberOfResources;j++) {
						possible = true;
						work[j] += temp_allocation[ci][j];
						finish[ci] = true;
					}
				}

			}
		}
		boolean answer = false;
		for (int i=0;i<finish.length;i++){
			if (finish[i]){
				answer = true;
			}
		}
		return answer;
	}
}