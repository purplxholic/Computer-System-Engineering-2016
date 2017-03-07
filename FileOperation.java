import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class FileOperation {
	private static File currentDirectory = new File(System.getProperty("user.dir"));
	public static void main(String[] args) throws java.io.IOException {

		String commandLine;

		BufferedReader console = new BufferedReader
				(new InputStreamReader(System.in));
		ProcessBuilder pBuilder = new ProcessBuilder();
		while (true) {
			// read what the user entered
			System.out.print("jsh>");
			commandLine = console.readLine();

			// clear the space before and after the command line
			commandLine = commandLine.trim();

			// if the user entered a return, just loop again
			if (commandLine.equals("")) {
				continue;
			}
			// if exit or quit
			else if (commandLine.equalsIgnoreCase("exit") | commandLine.equalsIgnoreCase("quit")) {
				System.exit(0);
			}

			// check the command line, separate the words
			String[] commandStr = commandLine.split(" ");
			ArrayList<String> command = new ArrayList<String>();
			for (int i = 0; i < commandStr.length; i++) {
				command.add(commandStr[i]);
			}

			// TODO: implement code to handle create here
			if (commandStr[0].equals("create")){
				Java_create(currentDirectory,commandStr[1]); 
			}

			// TODO: implement code to handle delete here
			else if(commandStr[0].equals("delete")){
				Java_delete(currentDirectory,commandStr[1]);
			}

			// TODO: implement code to handle display here
			else if(commandStr[0].equals("display")){
				Java_cat(currentDirectory,commandStr[1]);
			}

			// TODO: implement code to handle list here
			else if(commandStr[0].equals("list")){
				
				if (commandStr.length==1){ //list
					

					Java_ls(currentDirectory,"empty","empty");
				}
				else if(commandStr[1].equals("property")){ //list property & gang 	

					if (commandStr.length==2){ //list property only 

						Java_ls(currentDirectory,"property","empty");
					}
					else{

						Java_ls(currentDirectory,"property",commandStr[2]); 
					}
					
				}
				else {
					System.out.println("Invalid input");
				}
			}

			// TODO: implement code to handle find here
			else if(commandStr[0].equals("find")){
				if (commandStr.length==2){
					Java_find(currentDirectory,commandStr[1]);
				}
				else{
					System.out.println("Invalid input"); 
				}
				
			}

			// TODO: implement code to handle tree here
			else if (commandStr[0].equals("tree")){
				if (commandStr.length==1){ //tree
					count = Integer.MAX_VALUE; 
					Java_tree(currentDirectory,Integer.MAX_VALUE,"empty");
					
				}
				else if(commandStr.length==2){ //tree 1
//					System.out.println(commandStr[1]);
					if (Integer.parseInt(commandStr[1])==1){
						
						File[] files = currentDirectory.listFiles();
						for (File f:files){
							if (f.isDirectory()){
								System.out.println(f.getName());
							}
						}
					}
					else{
						count = Integer.parseInt(commandStr[1]);
						Java_tree(currentDirectory,Integer.parseInt(commandStr[1]),"empty");
					}
					
				}
				else{ //tree 1 time 
					count = Integer.parseInt(commandStr[1]);
					Java_tree(currentDirectory,Integer.parseInt(commandStr[1]),commandStr[2]);
				}
			}
			// other commands
//			ProcessBuilder pBuilder = new ProcessBuilder(command);
			else{
				pBuilder.directory(currentDirectory);
				try{
					Process process = pBuilder.start();
					// obtain the input stream
					InputStream is = process.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
	
					// read what is returned by the command
					String line;
					while ( (line = br.readLine()) != null)
						System.out.println(line);
	
					// close BufferedReader
					br.close();
				}
				// catch the IOexception and resume waiting for commands
				catch (IOException ex){
					System.out.println(ex);
					continue;
				}
			}
		}
	}

	/**
	 * Create a file
	 * @param dir - current working directory
	 * @param command - name of the file to be created
	 */
	public static void Java_create(File dir, String name) {
		// TODO: create a file
		File f = new File(dir,name);
		try {
			boolean createFile = f.createNewFile();
//			System.out.println("File creation status: "+ createFile); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * Delete a file
	 * @param dir - current working directory
	 * @param name - name of the file to be deleted
	 */
	public static void Java_delete(File dir, String name) {
		// TODO: delete a file
		
		File f = new File(dir,name);
		f.delete();
//		System.out.println("File deleted"); 
	}
	
	
	/**
	 * Display the file
	 * @param dir - current working directory
	 * @param name - name of the file to be displayed
	 */
	public static void Java_cat(File dir, String name) {
		// TODO: display a file
		File f = new File(dir,name); 
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String read; 
			while ((read =br.readLine())!=null){
				System.out.println(read);
			}
			br.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	/**
	 * Function to sort the file list
	 * @param list - file list to be sorted
	 * @param sort_method - control the sort type
	 * @return sorted list - the sorted file list
	 */
	private static File[] sortFileList(File[] list, String sort_method) {
		// sort the file list based on sort_method
		// if sort based on name
		if (sort_method.equalsIgnoreCase("name")) {
			Arrays.sort(list, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return (f1.getName()).compareTo(f2.getName());
				}
			});

		}
		else if (sort_method.equalsIgnoreCase("size")) {
			Arrays.sort(list, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.valueOf(f1.length()).compareTo(f2.length());
				}
			});

		}
		else if (sort_method.equalsIgnoreCase("time")) {
			Arrays.sort(list, new Comparator<File>() {
				public int compare(File f1, File f2) {
					
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				}
			});

		}

		
		
		
		return list;
	}

	/**
	 * List the files under directory
	 * @param dir - current directory
	 * @param display_method - control the list type
	 * @param sort_method - control the sort type
	 */
	public static void Java_ls(File dir, String display_method, String sort_method) {
		// TODO: list files
		String[] files = dir.list();
		File[] filesfiles = new File[files.length];
		for (int i=0;i<files.length;i++){
			filesfiles[i] = new File(files[i]);
		}
		sortFileList(filesfiles,sort_method);
		if (display_method.equals("empty")){
			for (File f:filesfiles){
				if (f.isDirectory()) System.out.println(f.getName());
				
			}
		}
		else{
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'SGT' yyyy");
			for(File f:filesfiles){
				System.out.println(f.getName() + " " + "Size: " + f.length() + " Last Modified: " + sdf.format(f.lastModified()));
			}
		}
		
		
		
	}


	/**
	 * Find files based on input string
	 * @param dir - current working directory
	 * @param name - input string to find in file's name
	 * @return flag - whether the input string is found in this directory and its subdirectories
	 */
	public static boolean Java_find(File dir, String name) {
		boolean flag = false;
		// TODO: find files
		if (dir.isDirectory()){
			for (File f:dir.listFiles()){
				if (f.isDirectory()){
					Java_find(f,name); //recursively find the other files 
				}
				else{
					if (f.getName().endsWith(name)){
						System.out.println(f.getAbsolutePath().toString()); //print out the absolute file 
						flag = true; 
					}
				}
			}
		}
		return flag;
	}

	/**
	 * Print file structure under current directory in a tree structure
	 * @param dir - current working directory
	 * @param depth - maximum sub-level file to be displayed
	 * @param sort_method - control the sort type
	 */
	static int count =0; //count is to keep track of sub directory
	public static void Java_tree(File dir, int depth, String sort_method) {
		// TODO: print file tree
		
		File[] filesfiles = dir.listFiles();
	
		sortFileList(filesfiles,sort_method);
		
		 

		if (filesfiles != null && filesfiles.length>0){

			for (File f: filesfiles){
				
				if (f.isDirectory()&&depth>1){
					
					if (depth < count){
						int n = count - depth; 
//						System.out.println(n);
						String n2 = ""; 
						for (int i=0;i<=n;i++){
							n2 += " "; 
						}
						System.out.println(n2+"|-" +f.getName());
						Java_tree(f,depth-1 ,sort_method);
					}
					else{
						System.out.println(f.getName());
						Java_tree(f,depth-1,sort_method);
					}
					
					
				}
				
				else{
					
					
						if (depth < count){
//							System.out.println("here 2");
							int n = count - depth; 
//							System.out.println(n);
							String n2 = ""; 
							for (int i=0;i<=n;i++){
								n2 += " "; 
							}
							System.out.println(n2+"|-" +f.getName());
							
						}
						else{
							System.out.println("  |-" + f.getName());
						}	
					
					
					
				}
			}
			
			
		}
		
	}

	// TODO: define other functions if necessary for the above functions


	
	

}