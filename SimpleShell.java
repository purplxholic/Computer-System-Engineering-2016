import java.io.*;
import java.util.*;
import java.util.*;

public class SimpleShell {
	
	static String currentadd;

	public static void main(String[] args) throws java.io.IOException{
		String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String dire;
		ArrayList <String> commandHistory = new ArrayList<String>(); //store the command list 
		String currentPath = System.getProperty("user.dir"); //saved here because PB keeps going back to the same directory 
		ProcessBuilder pb = new ProcessBuilder();

		while (true) {

			//read what the user entered
			System.out.print("jsh>");
			File newFile; 

			commandLine = console.readLine();
			//split the string into an array 
			String[] userCommand = commandLine.split(" "); 
			ArrayList <String> userCommander = new ArrayList<String>();
			
			//to manipulate the command list   
			for (String s: userCommand){
				userCommander.add(s);
//				System.out.println("ADDED: " +s);
			}
//			System.out.println("current commands: " + userCommander);
//			System.out.println("current history: " + commandHistory);
			
			
			
			
			//always add the commandLine if it's not blank 
			commandHistory.add(commandLine);

			//TODO: creating the external process and executing the command in that process
			try{
				//if a cd is detected and no blank afterwards 
				if (userCommander.get(0).equals("cd")){
					if (userCommander.size()>1){
						dire = userCommander.get(1);
//						System.out.println("DIRE " + dire);
						if (dire.equals("..")){			
														
							File temp = new File(currentPath);
//							System.out.println(System.getProperty("user.dir"));
//							File parentFile = new File(temp.getParent());
							String[] special = temp.getPath().split("/");
							String special2 = "";
							for (int k=0;k<special.length-1;k++){
								special2 += special[k] + "/";
							}
							File parentFile = new File(special2);
//							boolean check = parentFile.exists();
//							if (check){
//								System.out.println("The parent directory is " +parentFile);
				  				pb.directory(parentFile);
				  				//update current path to ensure file path is not stuck in where this java file is at 
				  				currentPath = parentFile.getPath();
//							}
							
						}
						//if not cd .. then it should be the directory
						else{
							String newDirectory = "";
							
							//creating the new directory by hard code 
							userCommander.remove(0); //take away cd 
							newDirectory = "/" + userCommander.get(0);
//							 System.out.println(newDirectory);
							 
							//eg cd a b 
							 if (userCommander.size() > 1){
								for (int j=1;j<userCommander.size();j++){
									 newDirectory = newDirectory + " " + userCommander.get(j);
									 							
								}
							 }
							 
//							 System.out.println("new dic:" + newDirectory);

							String actualNew = currentPath+ newDirectory;
//							System.out.println("new dic:" + actualNew);
							newFile = new File(actualNew);
							boolean check = newFile.isDirectory();
							
							if (check == false){
								System.out.println("No such directory found");

							}
							else{ 
								//open the directory and update it!
								pb.directory(newFile);
								currentPath = newFile.getPath();
								
							}
							
							
						}
						
					}
					
					//if user keys in blank after cd => brings to home 
					else if (userCommander.size()==1){
						newFile= new File(System.getProperty("user.home"));
						pb.directory(newFile);
						currentPath = newFile.getPath();
						
					}
					//if cd fails 
					else{
						System.out.println("NO SUCH DIRECTORY FOUND");
					}
										
				}
				//if the user entered a return, just loop again 
				else if (commandLine.equals("")){
					continue;
				}
				//TODO: adding a history feature 

					
				else if (userCommander.get(0).equals("history")){
//						System.out.println("NO CD DETECTED");
						if (commandHistory.size()==0){
							System.out.println("NO COMMAND HISTORY FOUND");
							continue;
						}
						for (int i=0;i<commandHistory.size();i++){
							
							System.out.println( i +"   " + commandHistory.get(i));
						}

						continue; 
						
					}
				
				//used regular expression to detect the numbers 
					
				else if (userCommander.get(0).matches("\\d?\\d?\\d")){
						int index = Integer.parseInt(userCommand[0]);
						System.out.println(index + "   "+ commandHistory.get(index));
						
					} 
	
				//execute previous command using !!
				else if (userCommander.get(0).equals("!!")){
//					System.out.println("!! detected");
					//if no history is dectected 
					if (commandHistory.size() ==0){
						System.out.println("ERROR! No previous command found. History is empty.");
					}
					else if (commandHistory.get(commandHistory.size()-2).equals("!!")){
						String newCommand = commandHistory.get(commandHistory.size()-3);
						String[] newCommand2 = newCommand.split(" "); 
						ArrayList<String> special = new ArrayList<String>();
						for (int m=0;m<newCommand2.length;m++){
							special.add(newCommand2[m]);
						}
					
					
						System.out.println("The previous command is " + userCommander.get(0));
				
						pb.command(special);
					}
					//special, just for history cos it's easy to handle
					else if (commandHistory.get(commandHistory.size()-2).equals("history")){
						if (commandHistory.size()==0){
							System.out.println("NO COMMAND HISTORY FOUND");
							continue;
						}
						for (int i=0;i<commandHistory.size();i++){
							
							System.out.println( i +"   " + commandHistory.get(i));
						}

						continue;
					}
					//redo the first part and it works wow!
					else{
						
						String newCommand = commandHistory.get(commandHistory.size()-2);
						String[] newCommand2 = newCommand.split(" "); 
					ArrayList<String> special = new ArrayList<String>();
					for (int m=0;m<newCommand2.length;m++){
						special.add(newCommand2[m]);
					}
					
					
//					System.out.println("The previous command is " + userCommander.get(0));
				
						pb.command(special);
//						System.out.println("EXECUTED");
				
					}
					
				}
				else{
//					System.out.println("command entered was not related to cd, history, !!, or index.");
							//if all else fails, just run the current command line eg get pwd
					pb.command(userCommand); 
					
					
				}
				
						
				///////START OF PROCESS//////
				Process p = pb.start();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String cOutput; 
				while ((cOutput = br.readLine()) != null){
					
					System.out.println(cOutput);
					
				}
				br.close();

				
			}
			catch (Exception e){
				System.out.println(e.getMessage() + ". Please re-type");
				
			}
			
		}
	}
}
